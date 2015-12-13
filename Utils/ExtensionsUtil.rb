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
end