ComicClean

A library and application for Comic Book Format files.

It loads Comic Book files (cbr, cbz, cbt, pdf) and saves (cbz, cbt, pdf).

The primary reason for this application was to explore different was of stripping out non-comic material from the files - files downloaded often contain logos, notices at the beginning and end of the 'pages'.

So we have a number of filters that can be applied to automatically detect and filter out bogus content.
- [x] Standard Size Deviation Filter looks for content that is not of a normal size, e.g. width and height of each page.
- [x] Size Deviation Filter where you can specify the deviation in pixels or as a percentage.
- [ ] File Name Frequency Filter which removes content who's file name is different from the majority. Its functioning but not complete.

All three approaches worked well on the files I have letting me automatically clean and strip comicbooks ready for use on eReaders.

- [ ] One improvement that could be made is to add a scrapper that correctly identifies and adds the Comic Book metadata that is now being supported, e.g. ComicBookInfo Metadata Format
(https://code.google.com/p/comicbookinfo/)
