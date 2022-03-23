## BAFinder

![BAFinder GUI](./doc/assets/BAFinder.png)

### Introduction
BAFinder is a program developed for the unknown bile acid identification from LC-MS/MS data in both positive and negative modes. It was developed in Java (jdk 15.0.1) with a graphical user interface (GUI) using the Open Source IDE Eclipse. BAFinder takes the alignment and peak list generated from XCMS (csv) and MS/MS spectra (MGF), searches them against a MS/MS reference library (MSP), a build-in characteristic feature (e.g. product ion or neutral loss) query list and an optional retention time library (txt) within user-defined m/z and RT tolerance, and exported the annotation summary (csv), processing details (csv) and representative MS/MS spectra (MGF) into an output folder.

### Download Link

BAFinder software<br> 
If you don't want to install java, please download the packaged program for Windows64 or macOS system.(~70 MB) <br> 
If you already have jre 15.0.1 or later installed, or don't mind installing java on your system, please download the jar file for Windows or macOS system. (~20 MB).

The following resources are also available for download:<br> 
User Manual (PDF)<br> 
MS/MS library for bile acids (MSP, including 84 bile acids, developed using QE-HFX in positive ESI mode at NCE 45 and negative ESI mode at NCE 60)<br> 
Test data (zip, example for input and output files of a human plasma dataset, with library included)

### Citation

If you use BAFinder software or libraries in your published work, please cite the BAFinder paper.

### Contact

If you have any question about BAFinder, please contact Dr. Yan Ma (mayan@nibs.ac.cn).


