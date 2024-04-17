_# Problem task

## Remarks

* the `ApplicationTester` class that reads a CSV file (using `OpenCSV`) can be used to run the simulation given a
  specific
  CSV file as argument, e.g. `src/main/resources/input2.csv`
  It should be noted that it (and the CSV file processing) is not meant to form part of the solution (with the focus
  being on the algorithm) and is merely present as a helper mechanism. As such no testing of this class was included
* when running `ApplicationTester`, the results are simply printed to the console in the
  format `<time>,<amount>,<account number>,<AlertType>`, where **_AlertType_** is either **Y** , **N**  or
  **S**. **S** means that the allowable number of accounts or transactions have been reached and the record
  will be skipped
* a strategy per account allows the flexibility to have different criteria for accounts (e.g. a higher limit for high
  net worth individuals)
* although no mechanism to remove transactions older than 60 seconds was implemented given the hard limit on the number
  of transactions and potential performance impact, this is something that would normally be discussed further with the
  relevant stakeholders. It should be noted that `IntegrationTest.stressTest()` was used to check the memory
  requirements given these upper limits, with further checks done in `VisualVM`
* when an alert is triggered an exception is thrown which could potentially be too expensive an operation, so an
  alternative might be preferable depending on the wider system design
* Amazon Coretto 17.0.10 was used for development

## Algorithm

* a `TreeMap` is used to store the stream of transaction entries. This allows for searches of runtime `O(log n)` to find
  the ceiling key, i.e. the first entry in the relevant time window
* each `TransactionDetail` entry contains a `prevCumulative` field which is the sum of all previous transaction amounts
  in the given window (i.e. time of this record - 60 seconds)
* with each new transaction, `prevCumulative` is calculated as: `prevCumulative` of the last successfully processed
  record - the `prevCumulative` of the first element in the new window, i.e. the sum of the elements no longer in the
  window are subtracted since the window has slided in time. If no record is found, `prevCumulative` is set to 0.
* each new `TransactionDetail` record is checked to see whether an existing record with the same time stamp already
  exists. If this is the case, the two records are combined into a new record since duplicate keys are not allowed in
  a `Map`

## Assumptions

* the records in the CSV file are well formatted with the first line being the header
* time is specified in 24h format and will not "roll over" to another day (adding a date will allow the support
  of different days in the same file while maintaining the chronological order)
* the solution is not thread safe since it was not specified and the nature of the input seemed to be sequential. It is
  however straightforward to implement if required; happy to discuss this further_