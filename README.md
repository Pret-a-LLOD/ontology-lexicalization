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
After the container is running do the followings:

### Endpoints and input/output specifications
All endpoints and parameter details can be found in [swagger document](https://app.swaggerhub.com/apis/melahi/lex-cbl/1.0.1)

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
- The DBpedia knowledge graph is large. It may take some time to download

### lexicalization
Given the DBpedia abstract and DBpedia knowledge base, the process will link linguistic terms (i.e. a token/a sequence of tokens) of the corpus with the content of DBpedia (i.e. predicate-object pair/restriction class, predicate, object, etc.)
- The process will first annotate the text with semantic information (i.e. annotating texts with rdfs: label) and then generates association rules to predict predicate-object pair, predicate, object, etc.
- The DBpedia is large and so it may take near 1 hour to get results for a class. For simplicity, we can run it for a class (i.e. Actor). It can be run for any class. 
- The system can be also run for all frequent classes (frequent 340 classes) of DBpedia but it will take more than a week to get results.
- write the class config.json contains the class.  The class list of DBpedia can be found here. 
- run the following command
```
curl -H POST "Accept: application/json" \
    -H "Content-type: application/json" \
    --data-binary @config.json \
    -X POST  http://localhost:8001/lexicalization
```
- All the parameters of input and output is detailed in [swagger document](https://app.swaggerhub.com/apis/melahi/lex-cbl/1.0.1)

### create lemon
- The process will create ontolex lemon for all linguistic terms
- run the following command
```
curl -H POST "Accept: application/json" \
    -H "Content-type: application/json" \
    --data-binary @config.json \
    -X POST  http://localhost:8001/createLemon
```
- All the parameters of input and output is detailed in [swagger document](https://app.swaggerhub.com/apis/melahi/lex-cbl/1.0.1)

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
