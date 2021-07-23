# Corpus-based lexicalization
The Corpus-based lexicalization (CBL) is a tool for bridging the lexical gap between content expressed in the form of natural language (NL) texts and content stored in an RDF knowledge base (KB). The current version of the tool is implemented for DBpedia. The Corpus-based lexicalization (CBL) is the core task of so-called ontology lexicalization [ontology lexicalization](https://aclanthology.org/W13-3803.pdf)

This page provides instructions on how to run the tool.

### Getting started with CBL
To run CBL on your machine follow the instructions.

## Requirements
- docker (https://docs.docker.com/engine/install/)

1. Download the image of CBL. 
```
docker pull pretallod/lex-cbl
```
2. Run the image as a container.
```
docker run -p 8001:8080 -t pretallod/lex-cbl
```

### Download linked data
Download DBpedia abstracts corpus and knowledge graph as follows: 
- If you have wget command installed in your terminal, download the configuration file for DBpedia
```
wget -O config.json https://github.com/Pret-a-LLOD/ontology-lexicalization/blob/master/config.json
```
- run the following command to download DBpedia abstracts and knowledge graph
```
curl -H POST "Accept: application/json" \
    -H "Content-type: application/json" \
    --data-binary @config.json \
    -X POST  http://localhost:8001/download 
```
- The parameter details can be found in [swagger document](https://app.swaggerhub.com/apis/melahi/lex-cbl/1.0.0)
- The DBpedia knowledge graph is large. It may take some time to download

# The below content is wrong and under construction  

### Link your terminology with other terminology
4.  Run the following command. Here, the other terminology is *intaglio terminology* (https://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_intaglio/sparql)
```
curl -d "endpoint=https://webtentacle1.techfak.uni-bielefeld.de/tbx2rdf_intaglio/sparql" \
          -H "Content-Type: application/x-www-form-urlencoded" \
          -X POST "http://localhost:8080/link"      
 ```
For example, *hole* is a term that exists both in your terminology and other terminology. To view the link, do the followings: 
- Browser: Click **Browser** button. Select alphabet pair **G_H** and then click the term **hole**. 
- Auto-completion search: Click **Terms** button and type *ho*. Select the term *hole*. 
- Sparql: Click **Sparql** button. Write the query for the term *hole* and then Click **Query** button.

Please use the following citation:
```
@inproceedings{Buono-LREC2020,
	title = {{Terme-`a-LLOD: Simplifying the Conversion and Hosting of TerminologicalResources as Linked Data}},
	author = {Maria Pia Di Buono, Philipp Cimiano, Mohammad Fazleh Elahi, Frank Grimm},
	booktitle = {Proceedings of the 7th Workshop on Linked Data in Linguistics (LDL-2020) at Language Resources and Evaluation Conference (LREC 2020)},
	pages = {28–35},
	year = {2020},
	location = {Marseille, France},
	publisher = {Association for Computational Linguistics},
	link = {https://lrec2020.lrec-conf.org/media/proceedings/Workshops/Books/LDL2020book.pdf}
}
```

## Developers
* **Mohammad Fazleh Elahi**
* **Frank Grimm**
## Supervisors
* **Maria Pia Di Buono**
* **Dr. Philipp Cimiano**  




---
