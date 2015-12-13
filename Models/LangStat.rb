class LangStat
    attr_accessor :language, :lineAdditions, :lineDeletions, :lineChanges
    def initialize(lang)
        @language = lang
        @lineAdditions = 0
        @lineDeletions = 0
        @lineChanges = 0
    end

    def to_s()
        str = "#{@language} {\n"
        str += "\t\tLine Additions: #{@lineAdditions}\n"
        str += "\t\tLine Deletions: #{@lineDeletions}\n"
        str += "\t\tLine Changes: #{@lineChanges}\n"
        str += "\t}\n"
    end

    def to_str()
        to_s()
    end
end