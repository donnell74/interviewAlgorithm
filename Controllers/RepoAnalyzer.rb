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
        @lastCommitDate = nil
        @myCommitsNearAnother = Array.new
        @otherCommitsNearAnother = Array.new
        @addedCommitsCount = 0
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
            if @addedCommitsCount % 10 == 0
                puts "Added #{@addedCommitsCount} out of unknown\n"
            end
            @addedCommitsCount += 1
            fullCommit = @client.commit("#{@client.user.login}/#{@repo.name}", eachCommit.sha)
            author = eachCommit.author

            newCommitDate = eachCommit.commit.author.date
            if not author.nil? and author.login == @client.user.login
                if !@lastCommitDate.nil?
                    # if new commit with in one hour of last
                    if newCommitDate < (@lastCommitDate - 3600)
                        @myCommitsNearAnother.push eachCommit
                    end
                end

                @myCommits.push eachCommit.commit
                addMyFiles(fullCommit)
            else
                if !@lastCommitDate.nil?
                    # if new commit with in one hour of last
                    if newCommitDate < (@lastCommitDate - 3600)
                        @otherCommitsNearAnother.push eachCommit
                    end
                end
                @otherCommits.push eachCommit.commit
                addOtherFiles(fullCommit)
            end

            @lastCommitDate = newCommitDate
        end
    end

    def addMyFiles(thisCommit)
        thisCommit.files.each do |eachFile|
            #puts thisCommit.commit.message
            #puts ExtensionsUtil.count(eachFile)
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
        str += "\tMy Commits within one hour of another: #{@myCommitsNearAnother.length}\n"
        str += "\tOther Commits within one hour of another: #{@otherCommitsNearAnother.length}\n"
        str += "\tMy Language Stats {\n"
        @myLangStats.each do |lang, eachStat|
            str += "\t\t" + eachStat
        end
        str += "\t\t}\n\tOther Language Stats {\n"
        @otherLangStats.each do |lang, eachStat|
            str += "\t\t" + eachStat.to_str()
        end
        str += ""
        str += "}\n"
    end

    def to_str()
        to_s()
    end
end