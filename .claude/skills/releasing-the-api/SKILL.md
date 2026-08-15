---
name: releasing-the-api
description: Use when cutting a release for skc-api — choosing the version, creating and pushing a version tag, or publishing GitHub release notes.
---

# Releasing the API

## Overview

skc-api is a single-project Kotlin / Spring Boot 4 WebFlux service built with Gradle and shipped as a Spring Boot fat JAR. It lives at
`ygo-skc/skc-api`; the default branch is `release`. Tags are bare
`vX.Y.Z`.

**There is exactly one version, in `build.gradle.kts`:**

```kotlin
version = "3.1.8"
```

It flows automatically: the `BootJar` task writes it into the JAR manifest as
`Implementation-Version`, and `AppConstants.APP_VERSION` reads it back with
`AppConstants::class.java.getPackage().implementationVersion ?: "LOCAL"`. Nothing else needs editing.

**Three things read that value**, so a stale version is wrong in three places at once:

| Reader             | Surfaces as                                                                         |
|--------------------|-------------------------------------------------------------------------------------|
| `StatusController` | `GET /api/v1/status` (base path is `spring.webflux.base-path` in `application.yml`) |
| `SwaggerConfig`    | the `version` of the served OpenAPI doc — the API's own published documentation     |
| `TrafficService`   | the `source.version` reported on every traffic call to skc-suggestion-engine        |

**The `LOCAL` trap:** `implementationVersion` is null unless the code is running *from a built JAR*. Under `./gradlew bootRun` or in the IDE,
`/api/v1/status` reports `LOCAL` — that is expected, not a bug, and it means the release version can only be confirmed by running the actual built
JAR.

## Pre-flight

Run from an up-to-date checkout of `release`.

```bash
grep -n '^version' build.gradle.kts
PREV=$(git tag --list 'v*' --sort=-v:refname | head -1)
git log --oneline "$PREV"..HEAD
git diff --stat "$PREV"..HEAD
./gradlew build
```

**`build.gradle.kts` is the source of truth — read it first.** If it already names an unreleased version (says `3.1.8` while the newest tag is
`v3.1.7`), that is the version to cut. Match it and skip the bump table. The bump lands in its own commit here and the tag goes on the branch tip
afterward, so the version commit is normally several commits behind HEAD — that is fine.

**If it is stale** — still naming the version `$PREV` already released — **stop. Do not tag.**
Report which version it should become and let the user commit and push that bump. A tag whose JAR manifest carries the previous version makes
`/api/v1/status` and the published OpenAPI doc report the wrong release, and nothing on the server will contradict it (see Common mistakes).

`./gradlew build` must pass. It is exactly what `.github/workflows/build.yaml` runs, and that workflow triggers on `tags: v**` — so anything failing
here becomes a permanent red check on an already-published tag. Note `build` also runs the jacoco floors from `gradle/unitTest.gradle.kts`
(LINE ≥ 30%, BRANCH ≥ 20%); a coverage regression fails the build even when every test passes.

`integTest` (Cucumber, needs a live API to hit) and `skcAPIPerf` (Gatling) are registered
`Verification` tasks that `build` does **not** run and CI never runs. Neither blocks a release —
`./gradlew build` passing is the whole gate.

## Choosing the version

Only needed when the version hasn't already been decided.

| Bump  | When                                                                                                                                                |
|-------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| Patch | Dependency roll-ups, bug fixes, config/log/perf tuning — no change to any response shape. Most releases here are this (`v3.1.2`, `v3.1.4`–`v3.1.7`) |
| Minor | A new capability or endpoint, or a framework migration that changes how the service is built or deployed (`v3.1.0`, Spring Boot 4 + UBI image)      |
| Major | A breaking change to the JSON contract or a wholesale re-architecture (`v3.0.0`, reactive rewrite that also dropped HATEOAS links)                  |

**The JSON contract is constrained by clients you cannot upgrade on demand.** Alongside `skc-site`
(web) and the `skc-suggestion-engine` / `skc-deck-api` services, this API is consumed by **`skc-swift` (iOS) and `skc-droid` (Android)** — shipped
through app stores, with installed versions in the wild indefinitely. A response-shape change strands those builds even when the web client ships in
lockstep, so treat it as breaking.

## Release notes

**Title:** `vX.Y.Z: Short Theme` when the release has a headline —
`v3.1.7: Hot Fix For Broken SSL Config`, `v3.1.0: Spring Boot 4 Migration + UBI Image Usage`. Themed titles are the norm here. Bare `vX.Y.Z` is for
pure dependency roll-ups (`v3.1.5`, `v3.1.6`).

**Body** is GitHub's generated notes with a hand-written description inserted at the top, under the heading:

```
## What's Changed
<hand-written description of what a human actually changed>

* Update Gradle to v9.7.0 by @renovate[bot] in <PR url>
* Update groovy monorepo to v4.0.33 by @renovate[bot] in <PR url>

**Full Changelog**: https://github.com/ygo-skc/skc-api/compare/<PREV>...<NEW>
```

Renovate automerges minor/patch here (`.github/renovate.json`), so most releases genuinely are dependency roll-ups — the generated PR list is the
content, not noise. Keep it. The hand-written part goes **above** it, separated by a blank line, and covers only what a human did.

Seed the file from GitHub rather than typing the PR list by hand:

```bash
gh api repos/ygo-skc/skc-api/releases/generate-notes \
  -f tag_name="vX.Y.Z" -f previous_tag_name="$PREV" --jq .body > notes.md
```

Then edit `notes.md` to insert the hand-written description under `## What's Changed`. Write it to a scratch directory, not into the repo.

**Releases before `v3.1.4` used a different format** — a hand-written `## Changes` heading with no PR list and no Full Changelog footer. That format
is retired. It is most of the release history by count, so it is what you hit first scrolling back; do not copy it.

## Sequence

Show the version, the diff, the `./gradlew build` result, and the drafted notes. Get approval **once**. Then run the rest without stopping again:

```bash
git tag vX.Y.Z <commit>          # lightweight: no -a, no -m
git push origin vX.Y.Z
gh api repos/ygo-skc/skc-api/releases/generate-notes \
  -f tag_name="vX.Y.Z" -f previous_tag_name="$PREV" --jq .body > notes.md
# insert the hand-written description under "## What's Changed"
gh release create vX.Y.Z --repo ygo-skc/skc-api \
  --title "vX.Y.Z" --notes-file notes.md
```

Push the tag first. `gh release create` attaches to an existing tag but invents one from the default branch when the tag is missing, and
`generate-notes` needs the tag to exist to compute the range.

## Why approval comes before the push

The pushed tag is what production is built from, and the GitHub Release is what API consumers see. Approval is the last cheap moment — after the push,
a wrong version is corrected with another release, not an edit.

## Common mistakes

- **`git tag -a`.** Every tag here is lightweight (`git cat-file -t v3.1.7` → `commit`). An annotated tag carries a message nobody reads; the notes
  belong in the GitHub Release.
- **Editing a version anywhere but `build.gradle.kts`.** There is no second copy. `AppConstants.kt`
  reads the JAR manifest — hardcoding a version there would break the one mechanism that keeps
  `/api/v1/status` honest.
- **Forgetting the version is also the public OpenAPI doc version.** `SwaggerConfig` feeds
  `APP_VERSION` into the served spec, so a stale version misreports the API's own documentation, not just `/status`.
- **Trusting `/api/v1/status` from a local run.** It says `LOCAL` unless the process was started from a built JAR. Only a real JAR run proves the
  version.
- **Assuming the deployed JAR's filename tells you the version.** `createDockerJar` renames
  `skc-api-3.1.8.jar` to `skc-api.jar` so Docker can mount a stable path — a stale deploy looks identical on disk and only `/api/v1/status` reveals
  it.
- **Running `./gradlew test` instead of `./gradlew build`.** `test` skips the jacoco coverage verification that CI enforces, so a coverage regression
  surfaces as a red check on a tag that is already public.
- **Treating a failing `integTest` or `skcAPIPerf` as a release blocker.** CI never runs them;
  `./gradlew build` is the gate.
- **Retyping the renovate PR list.** Use the `generate-notes` API; hand-copying it is how entries get dropped or point at the wrong PR.
- **Copying the pre-`v3.1.4` `## Changes` format.** It dominates the release history by count but is not the current convention.
