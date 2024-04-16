# Problem task

## Remarks

* the `ApplicationTester` class that reads a CSV file (using OpenCSV) can be used to run the simulation given a specific
  CSV file as argument, e.g. `src/main/resources/input2.csv`
  It should be noted that it (and the CSV file processing) is not meant to form part of the solution and is merely
  present as a helper
  mechanism (if needed) and as such no testing of this class was included. Typically, file output would be tested by
  creating predefined input&output files and compare the generated output file with the predefined one.
* when running `ApplicationTester`, the results are simply printed to the console in the
  format `<time>,<amount>,<account number>,<AlertType>`, where **_AlertType_** is either **Y** , **N**  or
  **S**. **S** means that the allowable number of accounts or transactions have been reached and the record
  will be skipped.
* a strategy per account allows the flexibility to have different criteria for accounts (e.g. a higher limit for high
  net worth individuals)
* although no mechanism to remove transactions older than 60 seconds was implemented, given the hard limit on the number
  of transactions and potential performance impact, this is something that would normally be discussed further with the
  relevant stakeholders
* when an alert is triggered an exception is thrown which could potentially be too expensive an operation, so an
  alternative might be preferable.
* Amazon Coretto 17.0.10 was used for development

## Algorithm

* a `TreeMap` is used to store the stream of transaction entries. This allows for searches of runtime `O(log n)` to find
  the floor key, i.e. the transaction just before the relevant window
* each `TransactionDetail` entry contains a `runningTotal` field which is the sum of all previous transaction amounts up
  to that point
* each new `TransactionDetail` record is checked to see whether an existing record with the same time stamp already exists. If this is the
  case, the two records are combined into a new record since duplicate keys are not allowed in a `Map`
* if the floor record is an exact match, the transaction amount of this record is also included in the window

## Assumptions

* time is specified in 24h format and will not "roll over" to another day (adding a date will allow the support
  of different days in the same file while maintaining the chronological order)
* the solution is not thread safe since it was not specified and the nature of the input seemed to be sequential. It is
  however straightforward to implement if required; happy to discuss this further if required