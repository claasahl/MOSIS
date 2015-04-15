Version 0.6 (2015-04-12)
========================
- Added a module for logging.
- Fixed several formatting, documention, and spelling issues.
- Largely revised tests.
- Added option to use composite nodes.
- Added new data handler for files.
- Replaced Relation-interface with Observer/Observable structure.


Version 0.55 (2014-03-09)
=========================
- Move "flow"-related classes into package "de.claas.mosis.flow" (e.g. Node and Link).
- Refined and improved "flow"-related tests (e.g. Iterator and Node tests).
- Refactored tests for data formats (e.g. PlainText and JSON tests).
- Added visitor design pattern for graph-based functions (e.g. initialization and processing).
- Documented parameters of Processor implementations.


Version 0.5 (2014-02-09)
========================
- Added support for initializing and dismantling of modules.
- Added support for EndOfStream (i.e. stop iterating at "end of file").
- Marked Relation as deprecated. It will be replaced by "Observer"-design pattern.


Version 0.4 (2013-10-14)
========================
- Added tests for large parts of the framework.
- Fixed several issues that appeared during testing.


Version 0.3 (2013-09-16)
========================
- Simplified iteration / processing of graphs.
- Added support for JSON (JavaScriptObjectNotation).
- Revised handling of CSV data.


Version 0.2 (2013-09-02)
========================
- Added support for CSV (CommaSeparatedValues).
- Added support for TCP-based streams.
- Documentation was largely revised.


Version 0.1 (2013-08-26)
========================
- Initial Announcement on mloss.org.
