class ExtensionsUtil
    @@androidIncludes = "java"
    @@cppIncludes = "cpp h"
    @@cssIncludes = "css"
    @@gradleIncludes = "gradle"
    @@htmlIncludes = "html"
    @@javaIncludes = "java"
    @@javascriptIncludes = "js"
    @@phpIncludes = "php"
    @@pyIncludes = "py"
    @@rbIncludes = "rb"
    @@txtIncludes = "txt"
    @@xmlIncludes = "xml"
    @@yamlIncludes = "yaml"

    @@androidTag = "Android"
    @@cppTag = "C++"
    @@cssTag = "CSS"
    @@gradleTag = "Gradle"
    @@htmlTag = "HTML"
    @@javaTag = "Java"
    @@javascriptTag = "JavaScript"
    @@phpTag = "PHP"
    @@pyTag = "Python"
    @@rbTag = "Ruby"
    @@txtTag = "Text"
    @@xmlTag = "XML"
    @@yamlTag = "YAML"

    def self.getLanguage(filename, repoLanguage)
        # get last extension of file, this will exclude .swp type of files
        segments = filename.split(".")
        repoLanguage = repoLanguage == nil ? "" : repoLanguage.downcase
        if segments.length > 0
            String extension = segments[segments.length - 1];
            if @@androidIncludes.include?(extension) and repoLanguage == "android"
                return @@androidTag
            elsif @@cppIncludes.include?(extension)
                return @@cppTag
            elsif @@cssIncludes.include?(extension)
                return @@cssTag
            elsif @@gradleIncludes.include?(extension)
                return @@gradleTag
            elsif @@htmlIncludes.include?(extension)
                return @@htmlTag
            elsif @@javaIncludes.include?(extension) and repoLanguage == "Java"
                return @@javaTag
            elsif @@javascriptIncludes.include?(extension)
                return @@javascriptTag
            elsif @@phpIncludes.include?(extension)
                return @@phpTag
            elsif @@pyIncludes.include?(extension)
                return @@pyTag
            elsif @@rbIncludes.include?(extension)
                return @@rbTag
            elsif @@txtIncludes.include?(extension)
                return @@txtTag
            elsif @@xmlIncludes.include?(extension)
                return @@xmlTag
            elsif @@yamlIncludes.include?(extension)
                return @@yamlTag
            end
        end

        return nil;
    end

    def self.count(file)
        skipMulti = false
        singleStartTag = " //" # keep space to avoid urls
        singleEndTag = "\\n"
        multiStartTag = "/\\*"
        multiEndTag = "\\*/"
        segments = file.filename.split(".")
        if segments.length > 0
            String extension = segments[segments.length - 1];
            if @@pyIncludes.include?(extension)
                singleStartTag = "#"
                multiStartTag = "\"\"\""
                multiEndTag = "\"\"\""
            elsif @@rbIncludes.include?(extension)
                singleStartTag = "#"
                multiStartTag = "=begin"
                multiEndTag = "=end"
            elsif @@htmlIncludes.include?(extension)
                singleStartTag = "<!--"
                singleEndTag = "-->"
                skipMulti = true
            end

            count = file.patch.scan(/#{singleStartTag}[^#{singleEndTag}]*#{singleEndTag}/).size
            if not skipMulti
                matchList = file.patch.scan(/#{multiStartTag}[^#{multiEndTag}]*#{multiEndTag}/)
                matchList.map{|x| x.count('\n')}.each do |numOfNewLines|
                    if numOfNewLines == 0 or numOfNewLines.nil?
                        count += 1
                    else
                        count += numOfNewLines
                    end
                end
            end
        end
    end
end