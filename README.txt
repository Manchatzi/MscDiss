The file contains two folders.

The first is the Java code that was used for the pre and post text mining processing.
It is a single script. Initial input is the .csv file of the collection.
First it creates the files that are going to be inputted in the geoparser and puts them in the subfolder /outs.
Since the geoparser is run separately, the code continues assuming that the geoparser has been executed, and the TSV files have been copied to the code directory under the subfolder /tsv.
Then it creates the 2 XML files that are needed for the web interface.
All these files (the csv, the /outs, the /tsvs, the XMLs) are already included in the folder, so the code should run without problem.

The second is the interface code. It is an HTML file named "test.html", that draws from the JS and CSS scripts that are also in this folder.
Use Mozilla Firefox to open it!!. Let it fully load before using any functionalities, or errors might occur.