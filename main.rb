require_relative 'Controllers/GithubAnalyzer'

# get config from netrc
repoAnalyzer = GithubAnalyzer.new()
puts repoAnalyzer