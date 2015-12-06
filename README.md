# interviewAlgorithm
Goal: Give programmers the ability to see a compatibility rating with a job posting.  This will make applying for jobs a learning process instead of a hit or miss process.

Future Versions:  
v0.1  
  Read all user's repos and generate the following statistics:  
    Total lines committed  
    Total lines committed for each language  
    Total languages (Ability to learn)  
    Total commits  
    Total commits within 30 minutes of another commit (communication)  
    Total merged pull request from other users (communication)  
    Total lines of documentation (wisdom)  
    Frequency of commits (endurance)  
    maybe more, maybe less  

v0.2  
  Get user's resume information from linkedin, need the follow:  
    User's name  
    Email  
    Past jobs (Search through for languages, project names, etc)  
    Languages  
    Education  
    
v0.3  
  Create git coverage report of linkedin profile  
    Show which languages are backed by git repositories, create list of up to ten commits for each language  
    Show which projects have git repositories  
    Show which languages are not back by git repositories  
    Don't show which projects don't have git repositories (company projects would always be red and would get annoying)  
    Show strength of each language (using some algorithm that accounts for total commits in language, last commit in language, 10000 hour rule, and other data that proves useful)  

v0.4  
  Given job description string, create a compatibility report  
    Show languages in common with just profile  
    Show languages in common with profile and git  
    Highlight projects that match job description  
    Show compatibility score (doesn't have to be right yet) (start with language strength vs years wanted in languages to compute score)  
    
v0.5  
  Compatibility report improvements  
  Add pipeline, Coverage at 30%  

v0.6  
  Database integration to store past reports  
    Add interest score to compatibility report (times compatibility score has increased)  
    Future feature: alert companies when they are being targeted by an applicant (interest score of 5+)  
  Coverage at 40%  

v0.7  
  Linkedin job postings  
  Coverage at 50%  

v0.8  
  More compatibility report improvements  
  Coverage at 60%  
  
v0.9  
  v0.9.0 - Team compatibility reports  
    Alert users when another user is searching for a similar job so they can create a team to work on a project to benefit both of them  
  v0.9.1 - Open source project compatibility reports  
    Alert users when there is an open source project that would help their compability score with a job description  
  Coverage at 70%  
    
v1.0  
  Get rid of programmer art, dead code, prettify code, get coverage above 80%  
