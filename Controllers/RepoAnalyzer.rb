require_relative '../Models/LangStat'
require_relative '../Utils/ExtensionsUtil'

class RepoAnalyzer
    attr_reader :repo

    def initialize(gitClient, repo)
        @client = gitClient
        @repo = repo
        @myCommits = Array.new
        @otherCommits = Array.new
        @myLangStats = Hash.new
        @otherLangStats = Hash.new
        self.getCommits
    end

    def name
        @repo.name
    end

    def getCommits()
        puts "#{@client.user.login}/#{@repo.name}"
        # get first page
        commitEndpoint = @client.commits("#{@client.user.login}/#{@repo.name}")
        nextPage = @client.last_response.rels[:next]
        addCommitPage(commitEndpoint)
        if nextPage.nil?
            return
        end

        # get the rest of the pages
        commitEndpoint = nextPage.get
        while true
            addCommitPage(commitEndpoint.data)
            if commitEndpoint.rels[:next] != nil
                commitEndpoint = commitEndpoint.rels[:next].get
            else
                break
            end
        end
    end

    def addCommitPage(endpoint)
        endpoint.each do |eachCommit|
            fullCommit = @client.commit("#{@client.user.login}/#{@repo.name}", eachCommit.sha)
            author = eachCommit.author
            if not author.nil? and author.login == @client.user.login
                @myCommits.push eachCommit.commit
                addMyFiles(fullCommit)
            else
                @otherCommits.push eachCommit.commit
                addOtherFiles(fullCommit)
            end
        end
    end

    def addMyFiles(thisCommit)
        puts "Adding commit to my files: #{thisCommit.commit.message}"
        thisCommit.files.each do |eachFile|
            lang = ExtensionsUtil.getLanguage(eachFile.filename, @repo.language)
            if lang.nil?
                next
            end
            if !@myLangStats.has_key?(lang)
                @myLangStats[lang] = LangStat.new(lang)
            end

            langStat = @myLangStats[lang]
            langStat.lineAdditions += eachFile.additions
            langStat.lineDeletions += eachFile.deletions
            langStat.lineChanges += eachFile.changes
        end
    end

    def addOtherFiles(thisCommit)
        puts "Adding commit to other files: #{thisCommit.commit.message}"
        thisCommit.files.each do |eachFile|
            lang = ExtensionsUtil.getLanguage(eachFile.filename, @repo.language)
            if lang.nil?
                next
            end
            if !@otherLangStats.has_key?(lang)
                @otherLangStats[lang] = LangStat.new(lang)
            end

            langStat = @otherLangStats[lang]
            langStat.lineAdditions += eachFile.additions
            langStat.lineDeletions += eachFile.deletions
            langStat.lineChanges += eachFile.changes
        end
    end

    def to_s()
        str = "Repo #{@repo.name} {\n"
        str += "\tMy Commits: #{@myCommits.length},\n"
        str += "\tOther Commits: #{@otherCommits.length},\n"
        str += "\tPercentage of Commits: #{@myCommits.length / (@myCommits.length + @otherCommits.length).to_f}\n"
        str += "\tMy Language Stats:\n"
        @myLangStats.each do |lang, eachStat|
            str += "\t" + eachStat
        end
        str += "\tOther Language Stats:\n"
        @otherLangStats.each do |lang, eachStat|
            str += "\t" + eachStat.to_str()
        end
        str += "}\n"
    end

    def to_str()
        to_s()
    end
end