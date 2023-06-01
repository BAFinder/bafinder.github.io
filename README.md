
## BART: a transferrable LC retention time library for bile acids
BART is a Java program to predict gradient retention times of bile acids on different LC-MS instruments based on isocratic retention modeling. <br> 
Currently the database contains 272 bile acids measured under the following LC condition:<br> 
Column: Waters BEH C18 (2.1 mm × 100 mm, 1.7 µm)<br> 
Mobile phase A: 7.5 mM ammonium acetate (adjusted to pH 4.9 using acetic acid) in water<br> 
Mobile phase B: ACN<br> 
Flow rate: 0.45 mL/min<br> 
Column temperature: 45 ℃<br> 

### Download BART
Program: for <a href="https://github.com/BAFinder/bafinder.github.io/tree/BART/software/BART_windows64.zip" download>Windows 64</a>, Mac OS(comming soon)<br> 
Library: c0, c1 and c2 parameters in the quadratic solvent strength model (QSSM) for 272 bile acids (<a href="https://github.com/BAFinder/bafinder.github.io/tree/BART/lib/BA_c0_c1_c2_lib.txt" download> txt </a> ) <br>
User Manual (<a href="https://github.com/BAFinder/bafinder.github.io/tree/BART/doc/BART User Manual.pdf" download>PDF</a>)<br> 
Test data: an example dataset (<a href="https://github.com/BAFinder/bafinder.github.io/tree/BART/data/test_data.zip" download> zip </a> ) <br>
Data for publication: validation datasets on five LC-MS systems (<a href="https://github.com/BAFinder/bafinder.github.io/tree/BART/data/data_for_publication.zip" download> zip </a> ) <br>

### Quick Start with Test Data
After extracting the contents of program and test data, change the file paths in the .bat file in the "test_data" folder according to their actual location and double click to run it. The output should be same as the "predictedRT_example.tsv" in the "test_data" folder. See User Manual for more details.

### Contact

If you have any question about BAFinder, please contact Dr. Yan Ma (mayan@nibs.ac.cn).




