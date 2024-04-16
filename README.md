# Problem task

## Remarks

* the `ApplicationTester` class that reads a CSV file can be used to run the simulation given a specific CSV file as
  input,
  e.g. `src/main/resources/input2.csv`
  It should be noted that it is not meant to form part of the solution and is merely present as a helper
  mechanism (if needed) and as such no testing of this class was included. OpenCSV is used to process the CSV file.
* when running `ApplicationTester`, the results are simply printed to the console in the
  format `<time>,<amount>,<account number>,<AlertType>`, where **_AlertType_** is either **Y** , **N**  or
  **S**. **S** means that the allowable number of accounts or transactions have been reached and the record
  will be skipped.
* a strategy per account allows the flexibility to have different criteria for accounts (e.g. a higher limit for high
  net worth individuals)
* although no mechanism to remove transactions older than 60 seconds was implemented, given the hard limit on the number of
  transactions and performance impact, this is something that would normally be discussed further with the relevant stakeholders 
* Amazon Coretto 17.0.10 was used for development

## Algorithm

* a `Treemap` is used to store the stream of transaction entries. This allows for searches of runtime `O(log n)` to find
  the floor key, i.e. the transaction just before the relevant window
* each entry contains a `runningTotal` field which is the sum of all transaction amounts up to that point
* each new record is checked to see whether an existing record with the same time stamp already exists. If this is the
  case, the two records are combined into a new record since duplicate keys are not allowed
* if the floor record is an exact match, the transaction amount of this record is also included in the window

## Assumptions

* time is specified in 24h format and will not "roll over" to another day (adding a date will allow the support
  of different days in the same file while maintaining the chronological order)
* the solution is not thread safe since it was not specified and the nature of the input seemed to be sequential. It is
  however straightforward to implement if required; happy to discuss this further if required