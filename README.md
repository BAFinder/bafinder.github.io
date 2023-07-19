## Tools for bile acid identification



### BART: a transferrable LC retention time library for bile acids
BART is a Java program to predict gradient retention times of bile acids on different LC-MS instruments based on isocratic retention modeling.  
Currently the database contains 272 bile acids measured under the following LC condition:<br> 
Column: Waters BEH C18 (2.1 mm × 100 mm, 1.7 µm)<br> 
Mobile phase A: 7.5 mM ammonium acetate (adjusted to pH 4.9 using acetic acid) in water<br> 
Mobile phase B: ACN<br> 
Flow rate: 0.45 mL/min<br> 
Column temperature: 45 ℃<br> 

#### Download BART
Program: for <a href="https://github.com/BAFinder/bafinder.github.io/tree/BART/software/BART_windows64.zip" download>Windows 64</a>, <a href="https://github.com/BAFinder/bafinder.github.io/tree/BART/software/BART_macosx.zip" download>Mac OS</a><br> 
Library: c0, c1 and c2 parameters in the quadratic solvent strength model (QSSM) for 272 bile acids (<a href="https://github.com/BAFinder/bafinder.github.io/tree/BART/lib/BA_c0_c1_c2_lib.txt" download> txt </a> ) <br>
User Manual (<a href="https://github.com/BAFinder/bafinder.github.io/tree/BART/doc/BART User Manual v1.2.pdf" download>PDF</a>)<br> 
Test data: an example dataset (<a href="https://github.com/BAFinder/bafinder.github.io/tree/BART/data/test_data.zip" download> zip </a> ) <br>
Data for publication: validation datasets on five LC-MS systems (<a href="https://github.com/BAFinder/bafinder.github.io/tree/BART/data/data_for_publication.zip" download> zip </a> ) <br>

#### Quick Start with Test Data
After extracting the contents of program and test data, change the file paths in the .bat file in the "test_data" folder according to their actual location and double click to run it. The output should be same as the "predictedRT_example.tsv" in the "test_data" folder. See User Manual for more details.



### BAFinder: unknown identification from MS/MS 
![BAFinder GUI](./doc/assets/BAFinder.png)

BAFinder is a program developed for the unknown bile acid identification from LC-MS/MS data in both positive and negative modes. It was developed in Java (jdk 15.0.1) with a graphical user interface (GUI) using the Open Source IDE Eclipse. BAFinder takes the alignment and peak list generated from XCMS (csv) and MS/MS spectra (MGF), searches them against a MS/MS reference library (MSP), a build-in characteristic feature (e.g. product ion or neutral loss) query list and an optional retention time library (txt) within user-defined m/z and RT tolerance, and exported the annotation summary (csv), processing details (csv) and representative MS/MS spectra (MGF) into an output folder.
#### New: BAFinder 2.0 is comming!
BAFinder 2.0 offers new functions to annotate amino-acid conjugated bile acids other than glycine and taurine. <br> 
For internal validation, please go to the following site to download the library, software and test data:<br> 
https://github.com/BAFinder/bafinder.github.io/tree/BAFinder-2.0 <br> 

#### Download BAFinder 1.0

BAFinder software<br> 
If you don't want to install java, please download the packaged program for <a href="./software/BAFinder_windows64.zip" download>Windows64</a> or <a href="./software/BAFinder_macosx.zip" download>macOS</a> system.(~70 MB) <br> 
If you already have jre 15.0.1 or later installed, or don't mind installing java on your system, please download the jar file for <a href="./software/BAFinder_win.jar" download>Windows</a> or <a href="./software/BAFinder_macosx.jar" download>macOS</a> system. (~20 MB).

The following resources are also available for download:<br> 
User Manual <a href="./doc/assets/BAFinder User Manual v1.0.pdf" download>(PDF)</a> <br> 
MS/MS library for bile acids (<a href="./data/MSMS_library.msp" download>MSP</a>, including 84 bile acids, developed using QE-HFX in positive ESI mode at NCE 45 and negative ESI mode at NCE 60)<br> 
Test data (<a href="./data/Test data.zip" download>zip</a>, example for input and output files of a human plasma dataset, with library included)



### Citation

If you use BAFinder software or libraries in your published work, please cite the BAFinder paper:<br> 
BAFinder: A Software for Unknown Bile Acid Identification Using Accurate Mass LC-MS/MS in Positive and Negative Modes<br> 
DOI:10.1021/acs.analchem.1c05648

### Contact

If you have any question about BAFinder, please contact Dr. Yan Ma (mayan@nibs.ac.cn).




