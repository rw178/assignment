# Problem task

## Remarks

* the `ApplicationTester` class can be used to run the simulation given a specific CSV file as input, e.g. `/input.csv`
  It should be noted that it is not meant to form part of the solution and is merely present as a convenient helper
  mechanism (if needed). As such no testing of this class was included.
* a strategy per account allows the flexibility to have different criteria for accounts (e.g. a higher limit for high
  net worth individuals)
* the solution is not thread safe since it was not requested (and the nature of the input seemed to be sequential),
  however it's straightforward to implement if needed; I'm happy to discuss this further
* when running `ApplicationTester`, the results are simply printed to the console in the
  format `<time>,<amount>,<account number>,<AlertType>`, where **_AlertType_** is either **Y** , **N**  or
  **S**. **S** means that the allowable number of accounts or transactions have been reached and the record
  will be ignored.
* Amazon Coretto 17.0.10 was used for development

## Algorithm

* A Treemap is used to store the stream of transaction entries. This allows for (O(log n)) searches to find the
  floor key, i.e. the transaction just before the relevant window.
* Each entry contains a `runningTotal` value which is the sum of all transaction amounts up to that point.
* Each new record is checked to see whether an existing record with the same time stamp already exists. If this is the
  case, the two records are combined into a new record since duplicate keys are not allowed.
* If the floor record is an exact match, the transaction amount should be included

## Assumptions

* it's assumed that the CSV file mechanism is merely used to demonstrate the process and as such no unit tests/checks
  were added for this
* time is specified in 24h format and will not exceed 23:59:59 in the input file (adding a date will allow the support
  different days)