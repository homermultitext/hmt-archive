# Guide to understanding HMT project publications (2018)

## What does the HMT project publish?

-   The HMT project does not develop software.
-   The HMT project creates long-lived scholarly data sets.

## What content is included HMT publications?

All material in HMT publications follows explicit models that do not depend on any specific technology.  The most important generic models are:

-   Visual evidence in the form of citable images (see <https://cite-architecture.github.io/imagemodel>)
-   Codex manuscripts and papyrus rolls modelled as sequences of text-bearing surfaces (see <https://cite-architecture.github.io/tbsmodel>)
-   Citable texts modelled as an ordered hierarchy of content objects (see https://cite-architecture.github.io/ohco2)
-   A digital scholarly edition modelled as relations among visual evidence, text bearing surfaces, and citable texts (see <https://cite-architecture.github.io/dse>)

The HMT project is developing a project-specific model of the contents of citable texts.  This model describes the contents of a citable passage of text in multiple layers from the editorial status of a string of characters through different levels of editorial disambiguation and interpretation of tokens. (Initial documentation: <https://homermultitext.github.io/hmt-editing-principles/>)


## What formats are used for HMT publications?


Contributors to the HMT assemble material in TEI-compliant XML files and in tabular delimited-text files.  An automated publication process composites all of the source material in a single text file in CEX format (specification linked [here](https://cite-architecture.github.io/citedx/).)


## How are the publications verified?

Before publication, a composite CEX file encoding the entire contents of the archive is analyzed for inconsistencies in content and structure.  A detailed listing of every error is recorded in a human-reable list of corrigenda.  A machine-generated textual summary and visualizations of different aspects of the publication are written as files in markdown format.

The automated verification depends on a number of code libraries.  To assess the quality of our automated evaluation of an edition, the main libraries that should be evaluated are:


-   `xcite`, a library for working with scholarly citation: <https://cite-architecture.github.io/xcite/>
-   `ohco2`, a libary for working with repositories of citable texts: <https://cite-architecture.github.io/ohco2/>
-   `citeobj`, a library for working repositories of citable objects:  <https://cite-architecture.github.io/citeobj/>
-   `scm`, a library for working with a library citable scholarly material: <https://cite-architecture.github.io/scm/>
-   `hmt-edmodel`, a library that parses the archival editions of the HMT project into text units with specified semantics:  <https://github.com/homermultitext/hmt-textmodel>

Each library includes a suite of automated tests, API documentation, and some additional end-user documentation.

## How do I find them?

Published releases, comprised of a single data set in `.cex` format, a catalog of corrigenda, and a folder with user-readable reports, are committed to the `releases-cex` directory of the project's `hmt-archive` github repository:
<https://github.com/homermultitext/hmt-archive/tree/master/releases-cex>

While the rest of the archive is constantly changing, files committed to this directory should be immutable.  Instead of updating them, new uniquely identified releases are committed to the same directory.

## What software can I use to work with HMT publications?

Since CEX files are just plain-text files, you can use any tools that work with text.  For example:

-   You can inspect or browse the an HMT publication with a text editor, search it with command-line tools `grep`, or of course read the file in any scripting language.
-   Individual labelled CEX blocks are easily imported into databases or statistical software.
-   CITE-App is an browser-based app for working with data loaded from a CEX source:
<https://github.com/cite-architecture/CITE-App>



## What other kinds of access does the project offer?

The principal publication is the CEX source that can be downloaded from github.  In addition, we currently offer:

-   a REST returning json [DOCUMENTATION TBA]
-   an installation of CITE-App: <http://www.homermultitext.org/cite-app/>
