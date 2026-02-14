Feature: Persistence of gym instances between server restarts

  Assuming something triggers server restart.
  Gym instances must be able to save their state into world save folder.
  After server restart gym instances must be restored
  so players should be able to finish the instance
  and get the rewards and/or exit the gym instance correctly.

  Scenario: Server initializes shutdown process
    Given there are gym instances in memory
    And there are players in them
    Then all gym instances save their data to world save

  Scenario: Server launches and ready
    Given there are gym instances in world save
    Then server reads gyms world save data into memory
    And when the player logs back into gym instance
    Then player should be able to complete it

