# gradedReaderBuilderServer
_A server-ised version of https://github.com/IdiosApps/gradedReaderBuilder_

Graded Reader Builder lets people create professional-quality graded readers from simple text input (story, vocab, characters, etc.). A wealth of high-quality books written by language learners, for language learners can be produced by this tool!

## Graded Reader Builder Input & Output

<img src="https://github.com/IdiosApps/gradedReaderBuilder_deprecated/blob/master/examples/Graded-Reader-Builder-OutputExample.png" width="400">
<img src="https://github.com/IdiosApps/gradedReaderBuilder_deprecated/blob/master/examples/Graded-Reader-Builder-Vocab(CN-EN).png" width="250">

## Features
### Supported language pairs:
* L2 Mandarin (Hanzi/Hanzi(+pinyin in footers), L1 English
* L2 English, L1 Mandarin (TESTING)

#### Graded-reader features:
* All vocabulary words are superscripted in the story, with the form "page.vocab number".
* Each page with new vocabulary has left and right footers (which will split e.g. 3 words into 2 on the left, 1 on the right).
* Key names are underlined in the story.

The example pdf is available [here](https://github.com/IdiosApps/gradedReaderBuilder_deprecated/blob/master/examples/ExampleGradedReader.pdf)



# Users
Please await a RESTful webapp! :)

# Developers
In IntelliJ, clone from Git source. 
Gradle may not be happy (JRE / gradle support mismatch - something like that, forgot) - if so:
go to `File/Project Structure/Project` and set the SDK to Java `version 12`. *Refresh* gradle build. Everything should be good!

TODO: I need to update this, as it's ~ a copy-paste of the readme on my JavaFX app version of Graded Reader Builder (which is a bit of a faff to set up; available at https://github.com/IdiosApps/gradedReaderBuilder)

**If you encounter a bug, please open an issue so we can make various bug fixes and performance improvements ;)**

##Prerequisites for developers
You may benefit from having certain fonts installed. For Windows users:
1. Make sure the languages you want to work with are installed in Windows.
2. Install [Google fonts](https://github.com/google/fonts). You can install these on a per-language basis, or install 
all .ttf files by running this Powershell command in the extracted folder (thanks to [Guss & EvgeniySharapov @ Stack Exchange](https://superuser.com/a/788759/485752):

`$fonts = (New-Object -ComObject Shell.Application).Namespace(0x14) >> Get-ChildItem -Recurse -include *.ttf | %{ $fonts.CopyHere($_.fullname) }`

Ideally the confirmation dialogue for each font could be skipped.
