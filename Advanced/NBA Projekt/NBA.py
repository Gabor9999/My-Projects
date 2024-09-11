from nba_api.stats.static import players,teams
#Find players
#Find teams
from nba_api.stats.endpoints import playernextngames, commonteamroster, playercareerstats, playergamelog, playergamelogs, boxscoretraditionalv2, franchiseplayers
#Face stats
#Box score classic/game
#Game log
#Players for each team
import pandas as pd
from time import sleep

#all_players = pd.read_csv('active_rotation_ids.csv')

def get_all_player_ids():
    df2 = pd.DataFrame(teams.get_teams()).filter(items=['id'])
    for tid in df2['id']:
        playersperteam = franchiseplayers.FranchisePlayers(league_id = '00',per_mode_detailed ='Totals',season_type_all_star='Regular Season',team_id=str(tid))
        headers = playersperteam.franchise_players.get_dict()['headers']
        data = playersperteam.franchise_players.get_dict()['data']
        df = pd.DataFrame(data, columns=headers)
        roster = df[df['ACTIVE_WITH_TEAM'] == 1].sort_values(by=['PTS'], ascending=False).head(12).filter(items=['TEAM','PLAYER','PERSON_ID'])
        all_players = pd.concat([all_players,roster], axis=0)
    all_players.to_csv('active_rotation_ids.csv', index=False) 
# Nikola Jokić
#career = playercareerstats.PlayerCareerStats(player_id='203999') 
#print(career.get_data_frames()[0])

#lebron = players.find_players_by_first_name('lebron')
#print(lebron)
#lakers = teams.find_teams_by_full_name('lakers')
#print(lakers[0]['id'])

#info = commonplayerinfo.CommonPlayerInfo(player_id=str(lebron[0]['id']))
#headers = info.player_headline_stats.get_dict()['headers']
#data = info.player_headline_stats.get_dict()['data']
#df = pd.DataFrame(data, columns=headers)
#info = boxscoretraditionalv2.BoxScoreTraditionalV2(end_period='1',end_range='0',game_id='0021700807',range_type='0',start_period='1',start_range='0')
#headers = info.player_stats.get_dict()['headers']
#data = info.player_stats.get_dict()['data']
#df = pd.DataFrame(data, columns=headers)
#playersperteam = franchiseplayers.FranchisePlayers(league_id = '00',per_mode_detailed ='Totals',season_type_all_star='Regular Season',team_id=str(lakers[0]['id']))
#info = playergamelog.PlayerGameLog(player_id=str(lebron[0]['id']),season='2023-24',season_type_all_star='Regular Season')
#headers = info.player_game_log.get_dict()['headers']
#data = info.player_game_log.get_dict()['data']
#df = pd.DataFrame(data, columns=headers)
#a = df.head(5).filter(items = ['MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS']).mean().round(2)
#b = df[df['MATCHUP'].str.contains("NOP")].filter(items = ['MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS']).mean().round(2)
#newdata = pd.DataFrame([a,b],index = ['Utolsó 5 meccs','Szezon átlag NO ellen'])
#print(newdata)
'''
headers = playersperteam.franchise_players.get_dict()['headers']
data = playersperteam.franchise_players.get_dict()['data']
df = pd.DataFrame(data, columns=headers)

roster = df[df['ACTIVE_WITH_TEAM'] == 1].sort_values(by=['PTS'], ascending=False).head(12).filter(items=['TEAM','PLAYER','PERSON_ID'])
stats = pd.DataFrame(columns = ['PERSON_ID','MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS'])
i = 0
last_5_games = stats
last_10_games = stats
matchup_season_games = stats
for player in roster['PERSON_ID']:
    info = playergamelog.PlayerGameLog(player_id=str(player),season='2023-24',season_type_all_star='Regular Season')
    headers = info.player_game_log.get_dict()['headers']
    data = info.player_game_log.get_dict()['data']
    df = pd.DataFrame(data, columns=headers)
    a = df[df['MIN'] > 5].head(5).filter(items = ['MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS']).mean().round(2)
    b = df[df['MIN'] > 5].head(10).filter(items = ['MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS']).mean().round(2)
    c = df[df['MATCHUP'].str.contains("NOP")].filter(items = ['MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS']).mean().round(2)
    newdata = pd.DataFrame([a]) #index = ['Utolsó 5 meccs','Utolsó 10 meccs','Szezon átlag NO ellen'])
    newdata.insert(0,'PERSON_ID',[player],True)
    last_5_games = pd.concat([last_5_games,newdata],axis = 0)
    i += 1
last_5_games = roster.join(last_5_games.set_index('PERSON_ID'),on='PERSON_ID')
print(last_5_games)
'''
'''
stats = pd.DataFrame(columns = ['PERSON_ID','MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS'])
last_5_games = stats
last_10_games = stats
matchup_season_games = stats

for player in all_players['PERSON_ID']:
    info = playergamelog.PlayerGameLog(player_id=str(player),season='2023-24',season_type_all_star='Regular Season')
    info = playergamelog.PlayerGameLog(player_id=str(all_players['PERSON_ID'][0]),season='2023-24',season_type_all_star='Regular Season').get_data_frames()
    info2 = info[0][-5:].filter(items=['Player_ID','MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS']).mean().round(2)
    #b = df[df['MIN'] > 5].head(10).filter(items = ['MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS']).mean().round(2)
    #c = df[df['MATCHUP'].str.contains("NOP")].filter(items = ['MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS']).mean().round(2)
    #newdata = pd.DataFrame([a])
    #newdata.insert(0,'PERSON_ID',[player],True)
    print(info2)
    #last_5_games = pd.concat([last_5_games,info2],axis = 0)
    #newdata = pd.DataFrame([b])
    #newdata.insert(0,'PERSON_ID',[player],True)
    #last_10_games = pd.concat([last_10_games,newdata],axis = 0)
    #newdata = pd.DataFrame([c])
    #newdata.insert(0,'PERSON_ID',[player],True)
    #matchup_season_games = pd.concat([matchup_season_games,newdata],axis = 0)
'''
#last_5_games = all_players.join(last_5_games.set_index('PERSON_ID'),on='PERSON_ID')
#last_10_games = all_players.join(last_10_games.set_index('PERSON_ID'),on='PERSON_ID')
#matchup_season_games = all_players.join(matchup_season_games.set_index('PERSON_ID'),on='PERSON_ID')
#print(last_5_games)
#print(last_10_games)
#print(matchup_season_games)

def print_menu():
    print("----------------------------------------------------------------------------------------------------")
    print("0 - Get Player Data")
    print("1 - Get Roster Data")
    print("Anything else - Exit")
    print("----------------------------------------------------------------------------------------------------")
    main = input('Choose option: \n') 
    if(main == '0'):
        player_search()
    elif (main == '1'):
        roster_search()
    else:
        exit()

def player_search():
    print("----------------------------------------------------------------------------------------------------")
    inp = input('What player are you looking for? \n') 
    player = players.find_players_by_full_name(inp)
    print("----------------------------------------------------------------------------------------------------")
    print("Selected player: " + player[0]['full_name'])
    info = playercareerstats.PlayerCareerStats(player_id=str(player[0]['id']),per_mode36='PerGame').get_data_frames()
    print(info[0][-1:].filter(items=['GP','GS','MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS']).to_string(index=False))
    print("----------------------------------------------------------------------------------------------------")
    playerdata = playergamelog.PlayerGameLog(player_id=str(player[0]['id']),season='2023-24',season_type_all_star='Regular Season').get_data_frames()[0]
    print("0 - Game Logs")
    print("1 - Last Game")
    print("2 - Last 5 Games")
    print("3 - Last 10 Games")
    print("4 - Next Matchup Game Logs From This Season")
    print("B - Back to main menu")
    print("Anything else - Exit")
    print("----------------------------------------------------------------------------------------------------")
    inp = input('Choose option: \n') 
    match inp:
        case '0':
            print(playerdata)
            print_menu()
        case '1':
            print(playerdata[:0])
            print_menu()
        case '2':
            print(playerdata[:5])
            print_menu()
        case '3':
            print(playerdata[:10])
            print_menu()
        case '4':
            next = playernextngames.PlayerNextNGames(number_of_games="1",season_all="2023-24",season_type_all_star="Regular Season",player_id=str(202691)).get_data_frames()[0]
            print(next)
        case 'b':
            print_menu()
        case _:
            exit()

def roster_search():
    inp = input('What team are you looking for? (format: LAL or Lakers) \n') 
    team = teams.find_teams_by_full_name(inp)
    if(len(team) == 0):
        print("No team exists with that name")
        sleep(2)
    elif(len(team) > 1):
        print("Multiple teams found\n")
        print("Did you mean?\n")
        print("0 - " + team[0]['full_name'])
        for i in range(1, len(team)-1):
            print("or\n")
            print(i + "- " + team[i]['full_name'])
        inp = input("What number do you wish to proceed with? \n")
        team = team[inp]
    else:
        team = team[0]
        print("----------------------------------------------------------------------------------------------------")
        print("Selected team: " + team['full_name'])
        print("----------------------------------------------------------------------------------------------------")
        roster = commonteamroster.CommonTeamRoster(team_id=str(team['id']),season="2023-24")
        roster = roster.common_team_roster.get_data_frame().filter(items=['PLAYER','PLAYER_ID'])
        print("0 - Season Averages")
        print("1 - Last Game (Average)")
        print("2 - Last 5 Games (Average)")
        print("3 - Last 10 Games (Average)")
        print("4 - Next Matchup Data From This Season (Average)")
        print("B - Back to main menu")
        print("Anything else - Exit")
        print("----------------------------------------------------------------------------------------------------")
        inp = input('Choose option: \n')
        match inp:
            case '0':
                stats = pd.DataFrame(columns=['GP','GS','MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS'])
                print("Loading - Progress 0/" + str(len(roster)))
                for index, row in roster.iterrows():
                    info = playercareerstats.PlayerCareerStats(player_id=str(row['PLAYER_ID']),per_mode36='PerGame').get_data_frames()
                    info = info[0].filter(items=['GP','GS','MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS'])
                    stats = pd.concat([stats, info], ignore_index=True)
                    print("Loading - Progress " + str(index+1) + "/" + str(len(roster)))
                print("----------------------------------------------------------------------------------------------------")
                out = roster.join(stats)
                print(out)
                print_menu()
            case '1':
                stats = pd.DataFrame(columns=['GAME_DATE','MATCHUP','WL','MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS'])
                print("Loading - Progress 0/" + str(len(roster)))
                for index, row in roster.iterrows():
                    info = playergamelog.PlayerGameLog(player_id=str(row['PLAYER_ID']),season='2023-24',season_type_all_star='Regular Season').get_data_frames()
                    info = info[0][:0].filter(items=['GAME_DATE','MATCHUP','WL','MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS'])
                    stats = pd.concat([stats, info])
                    print("Loading - Progress " + str(index+1) + "/" + str(len(roster)))
                print("----------------------------------------------------------------------------------------------------")
                out = roster.join(stats)
                print(out)
                print_menu()
            case '2':
                stats = pd.DataFrame(columns=['MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS'])
                print("Loading - Progress 0/" + str(len(roster)))
                for index, row in roster.iterrows():
                    info = playergamelog.PlayerGameLog(player_id=str(row['PLAYER_ID']),season='2023-24',season_type_all_star='Regular Season').get_data_frames()
                    info = info[0][:5].filter(items=['MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS']).mean().round(2).to_frame().T
                    stats = pd.concat([stats, info], ignore_index=True)
                    print("Loading - Progress " + str(index+1) + "/" + str(len(roster)))
                print("----------------------------------------------------------------------------------------------------")
                out = roster.join(stats)
                print(out)
                print_menu()
            case '3':
                stats = pd.DataFrame(columns=['MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS'])
                print("Loading - Progress 0/" + str(len(roster)))
                for index, row in roster.iterrows():
                    info = playergamelog.PlayerGameLog(player_id=str(row['PLAYER_ID']),season='2023-24',season_type_all_star='Regular Season').get_data_frames()
                    info = info[0][:10].filter(items=['MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS']).mean().round(2).to_frame().T
                    stats = pd.concat([stats, info], ignore_index=True)
                    print("Loading - Progress " + str(index+1) + "/" + str(len(roster)))
                print("----------------------------------------------------------------------------------------------------")
                out = roster.join(stats)
                print(out)
                print_menu()
            case '4':
                next = playernextngames.PlayerNextNGames(number_of_games=1,season_all='ALL',season_type_all_star='Regular Season',player_id=str(202691)).get_data_frames()[0]
                print(next)
            case 'b':
                print_menu()
            case _:
                exit()
                
#print_menu()
teamsF = []
for i in teams.get_teams():
    teamsF.append(i['full_name'])
    teamsF.append(i['id'])
print(teamsF)