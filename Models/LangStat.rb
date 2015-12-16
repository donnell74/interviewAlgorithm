class LangStat
    attr_accessor :language, :lineAdditions, :lineDeletions, :lineChanges
    def initialize(lang)
        @language = lang
        @lineAdditions = 0
        @lineDeletions = 0
        @lineChanges = 0
        @byteAdditions = 0
        @byteDeleteions = 0
    end

    def to_s()
        str = "#{@language} {\n"
        str += "\t\t\tLine Additions: #{@lineAdditions}\n"
        str += "\t\t\tLine Deletions: #{@lineDeletions}\n"
        str += "\t\t\tLine Changes: #{@lineChanges}\n"
        str += "\t\t}\n"
    end

    def to_str()
        to_s()
    end
end