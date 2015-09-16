import groovy.io.FileType
import java.util.zip.ZipFile

println "Is about to list files in directories "

String directoryName = './'
def directory = new File(directoryName)
def words = []
directory.eachFileRecurse (FileType.FILES) { file ->
  if (file.path.endsWith('jar')){
    def zipFile = new ZipFile(file)
    zipFile.entries().findAll { !it.directory && it.name.endsWith('dic') }.each {
      println "Is about to collect words from ${it.name} inside ${file.path}"
      words << zipFile.getInputStream(it).text
    }
  }
}
new File(directory, 'new_words.txt') << words.join("\n")
