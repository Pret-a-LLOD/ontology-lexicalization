# Corpus-based lexicalization for DBpedia
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
After the container is running do the followings:

### lexicalization
- the DBpedia resource (abstracts, knowledge graph, and semantically annotated text/anchor text) are very large. Therefore, the container contains the DBpedia resource. 
- Given the DBpedia resource, the program will provide class-specific lexicalization. 
- The input file contains class and necessary parameteres to run the lexicalization process. The process will link linguistic terms (i.e. a token/a sequence of tokens) of the DBpedia abstracts of the class with predicate-object pair/restriction class, predicate, object, etc.
- The input file with example can be found in [swagger document](https://app.swaggerhub.com/apis/melahi/lex-cbl/1.0.1)
- Download input file. If you have wget command installed in your terminal, download the configuration file for DBpedia

```
wget -O config.json https://github.com/Pret-a-LLOD/ontology-lexicalization/blob/master/config.json
```
- The process will first annotate the text with semantic information (i.e. annotating texts with rdfs: label) and then generates association rules to predict predicate-object pair, predicate, object, etc.
- The DBpedia is large and so it may take nearly 1 hour to get results for a class. For simplicity, we can run it for a class (i.e. Actor). It can be run for any class. 
- The system can be also run for all frequent classes (frequent 340 classes) of DBpedia but it will take more than a week to get results.
- write the class config.json contains the class.  The class list of DBpedia can be found here. 
- run the following command
```
curl -H POST "Accept: application/json" \
    -H "Content-type: application/json" \
    --data-binary @config.json \
    -X POST  http://localhost:8001/lexicalization
```

### create lemon
- The process will post process the data and create ontolex lemon 
- The input file with example can be found in [swagger document](https://app.swaggerhub.com/apis/melahi/lex-cbl/1.0.1)
- run the following command
```
curl -H POST "Accept: application/json" \
    -H "Content-type: application/json" \
    --data-binary @config.json \
    -X POST  http://localhost:8001/createLemon
```

## CBL code
The project contains code of Perl and Java.

## Reference
Please use the following citation:
```
@inproceedings{Buono-LREC2020,
	title = {{Bridging the gap between Ontology and Lexicon via Class-specific Association Rules Mined from a Loosely-Parallel Text-Data Corpus}},
	author = {Basil Ell, Mohammad Fazleh Elahi, Philipp Cimiano},
	booktitle = {Proceedings of the  3rd Conference on Language, Data and Knowledge (LDK 2021)},
	year = {2021},
	location = {Zaragoza, Spain},
	link = {https://pub.uni-bielefeld.de/record/2954753}
}
```

## Developers
* **Mohammad Fazleh Elahi**
* **Basil Ell**
## Supervisors
* **Dr. Philipp Cimiano**  




---
