# accurate-file-finder
I only made this because Windows File Explorer was being a bitch. Otherwise, no big deal.

Anyone from stackoverflow (or basic understanding of recursion) can do this. lol

# Usage
Simply run the JAR file, and enter two things:
1. The directory you want to search

Whether it's the root of a drive, or a particular folder, the program will look for all files, folders and subfolders.

2. The file name (or part of it);

Now, for the file name, you can use the asterisk character (*) at the beginning or end of the file name.

If put at the beginning, the program will accept any file name. Otherwise, it will accept any file extension.

# Performance Results
Case A: Work PC with 8 GB RAM
* 40719 navigations total (Search All Files)
* 37876 files found in 1 min 17.129 s
* Speed: 528 navigations / s

Case B: Home PC with 4 GB RAM
* 40721 navigations total (Search All Files)
* 37878 files found in 8 min 17.499 s
* Speed: 82 navigations / s

Case C: Friend PC with 16 GB RAM
* 213997 navigations total (Search ADX Files)
* 14015 files found in 2 min 31.772 s
* Speed: 1409 navigations / s
