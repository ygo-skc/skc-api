@Card
Feature: Search the database and return available criteria a user can use when browsing for cards. Will only return criteria that can be found in the database.

  Scenario: Client requests all browsing criteria for Cards in the SKC database.
    When user requests browsing criteria
    Then browse criteria request status should be 200 and body should contain 8 elements
    And cardColor criteria array should contain the following:
      | Effect           |
      | Fusion           |
      | Link             |
      | Normal           |
      | Pendulum-Effect  |
      | Pendulum-Fusion  |
      | Pendulum-Normal  |
      | Pendulum-Ritual  |
      | Pendulum-Synchro |
      | Pendulum-Xyz     |
      | Ritual           |
      | Spell            |
      | Synchro          |
      | Trap             |
      | Xyz              |
    And attributes array should contain the following:
      | Dark   |
      | Divine |
      | Earth  |
      | Fire   |
      | Light  |
      | Water  |
      | Wind   |
    And monsterTypes array should contain the following:
      | Aqua          |
      | Beast         |
      | Beast-Warrior |
      | Cyberse       |
      | Dinosaur      |
      | Divine-Beast  |
      | Dragon        |
      | Fairy         |
      | Fiend         |
      | Fish          |
      | Illusion      |
      | Insect        |
      | Machine       |
      | Normal        |
      | Plant         |
      | Psychic       |
      | Pyro          |
      | Reptile       |
      | Rock          |
      | Sea Serpent   |
      | Spellcaster   |
      | Thunder       |
      | Warrior       |
      | Winged Beast  |
      | Wyrm          |
      | Zombie        |
    And levels array should contains the following:
      | 0  |
      | 1  |
      | 2  |
      | 3  |
      | 4  |
      | 5  |
      | 6  |
      | 7  |
      | 8  |
      | 9  |
      | 10 |
      | 11 |
      | 12 |
    And ranks array should contains the following:
      | 0  |
      | 1  |
      | 2  |
      | 3  |
      | 4  |
      | 5  |
      | 6  |
      | 7  |
      | 8  |
      | 9  |
      | 10 |
      | 11 |
      | 12 |
      | 13 |
    And link rating array should contains the following:
      | 1 |
      | 2 |
      | 3 |
      | 4 |
      | 5 |
      | 6 |