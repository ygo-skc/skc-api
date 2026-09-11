---
name: releasing-the-api
description: Use when cutting a release for skc-api — choosing the version, creating and pushing a version tag, or publishing GitHub release notes. Not for building, testing, or deploying the service; the user does those.
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
```

**`build.gradle.kts` is the source of truth — read it first.** If it already names an unreleased version (says `3.1.8` while the newest tag is
`v3.1.7`), that is the version to cut. Match it and skip the bump table. The bump lands in its own commit here and the tag goes on the branch tip
afterward, so the version commit is normally several commits behind HEAD — that is fine.

**If it is stale** — still naming the version `$PREV` already released — **stop. Do not tag.**
Report which version it should become and let the user commit and push that bump. A tag whose JAR manifest carries the previous version makes
`/api/v1/status` and the published OpenAPI doc report the wrong release, and nothing on the server will contradict it (see Common mistakes).

## Do not build, test, or deploy

**The user builds, tests, and deploys this service. You do not.** Your part starts at the version check and ends when the GitHub Release is
published.

Never run — not to "verify", not to "be safe", not in a scratch worktree:

- `./gradlew build`, `test`, `check`, `bootJar`, `bootRun`, `integTest`, `skcAPIPerf`, or any other Gradle task
- any Docker build, image push, registry login, `scp`, `ssh`, service restart, or rollout
- any command whose purpose is to discover how the service is built or deployed

CI already covers the build. `.github/workflows/build.yaml` triggers on `tags: v**`, so pushing the tag runs the real gate on GitHub's runners —
including the jacoco floors from `gradle/unitTest.gradle.kts` (LINE ≥ 30%, BRANCH ≥ 20%). Running it locally first duplicates that and delays the
release; it does not make the tag safer. If CI goes red, report it and let the user decide.

**"Deploy it" is not a request for this skill.** Say plainly that the release is where your part ends, and leave it there. Do not go looking for a
deploy mechanism first — the search is already the mistake.

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

Show the version, the diff, and the drafted notes. Get approval **once**. Then run the rest without stopping again:

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
- **Running any Gradle task.** Building locally is not part of a release here — not `test`, not `build`, and not in a throwaway worktree where
  "nothing gets touched". CI runs the gate when the tag lands.
- **Acting on "deploy it".** Deployment belongs to the user. Do not run it, script it, or investigate how it works.
- **Retyping the renovate PR list.** Use the `generate-notes` API; hand-copying it is how entries get dropped or point at the wrong PR.
- **Copying the pre-`v3.1.4` `## Changes` format.** It dominates the release history by count but is not the current convention.

## Rationalizations

| Excuse | Reality |
|--------|---------|
| "A quick `./gradlew build` makes the tag safer" | CI runs it on the tag regardless. A local run only delays the release. |
| "It's mostly cached, it'll take two seconds" | A cached build proves nothing about the tree, and speed was never the objection. |
| "I'll build in a temp worktree so the working tree stays clean" | Isolation is not the issue. Not running it is the instruction. |
| "The version commit is at HEAD — I should confirm it still compiles" | The user confirms that. Push the tag and let CI report. |
| "There are uncommitted changes, so I should check what they break" | Report the uncommitted changes and ask. Do not build to find out. |
| "They said deploy, so deploying is authorized" | Permission is not procedure, and this skill has none. The user deploys. |
| "I'll just peek at the workflows to see how deploys work" | Investigating the deploy path is the first step of deploying. Don't take it. |

## Red flags — stop

- Typing `./gradlew` anything
- `git worktree add` in order to "verify" a build
- `ls .github/workflows`, or grepping for `docker`, `deploy`, `compose`, `k8s`, `helm`
- Reporting a build, test, or coverage result as part of a release summary
- Treating "deploy it" as in scope

**All of these mean: stop, and hand it back to the user.**
