require 'Octokit'
require_relative 'RepoAnalyzer'

class GithubAnalyzer
    def initialize()
        @client = Octokit::Client.new(:netrc => true)
        @client.login
        @repoAnalyzers = Array.new
        @client.repos.each do |eachRepo|
            begin
                if ARGV.include?(eachRepo.name()) or ARGV.length == 0
                    @repoAnalyzers.push RepoAnalyzer.new(@client, eachRepo)
                end
            rescue Octokit::NotFound

            end
        end
    end

    def to_s()
        resStr = ""
        @repoAnalyzers.each do |eachRepo|
            resStr += eachRepo
        end
        resStr
    end
end