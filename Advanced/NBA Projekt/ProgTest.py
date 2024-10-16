import tkinter as tk
from PIL import Image, ImageTk
from nba_api.stats.endpoints import playergamelogs, teamplayerdashboard, playerestimatedmetrics, playerdashboardbylastngames
from nba_api.stats.static import players
from pandastable import Table
import pandas as pd
import threading
import requests

#url = "https://data.oddsblaze.com/v1/odds/draftkings_nfl.json?key=your_key"
#response = requests.get(url)
#print(response.json())

teamsCont = ['Atlanta Hawks', 'Boston Celtics', 'Cleveland Cavaliers', 'New Orleans Pelicans', 'Chicago Bulls', 'Dallas Mavericks', 'Denver Nuggets', 'Golden State Warriors', 'Houston Rockets', 'Los Angeles Clippers', 'Los Angeles Lakers', 'Miami Heat', 'Milwaukee Bucks', 'Minnesota Timberwolves', 'Brooklyn Nets', 'New York Knicks', 'Orlando Magic', 'Indiana Pacers', 'Philadelphia 76ers', 'Phoenix Suns', 'Portland Trail Blazers', 'Sacramento Kings', 'San Antonio Spurs', 'Oklahoma City Thunder', 'Toronto Raptors', 'Utah Jazz', 'Memphis Grizzlies', 'Washington Wizards', 'Detroit Pistons', 'Charlotte Hornets']
teamIDs = [1610612737, 1610612738, 1610612739, 1610612740, 1610612741, 1610612742, 1610612743, 1610612744, 1610612745, 1610612746, 1610612747, 1610612748, 1610612749, 1610612750, 1610612751, 1610612752, 1610612753, 1610612754, 1610612755, 1610612756, 1610612757, 1610612758, 1610612759, 1610612760, 1610612761, 1610612762, 1610612763, 1610612764, 1610612765, 1610612766]
# Python version 3.12 warning
pd.set_option("future.no_silent_downcasting", True)

class App(threading.Thread):
    def __init__(self):
        #super().__init__()
        threading.Thread.__init__(self)
        self.start()

    def callback(self):
        self.root.quit()

    def run(self):
        self.root = tk.Tk()
        self.root.protocol("WM_DELETE_WINDOW", self.callback)

        self.root.title("NBA Analysis Project")
        self.root.geometry('1280x720')
        self.root.minsize(1280,720)

        sidemenuFrame = tk.Frame(master=self.root,width=300,height=600,bg="grey")
        sidemenuFrame.pack(fill=tk.BOTH, side=tk.LEFT)

        btn_Player = tk.Button(sidemenuFrame, text="Search For Player Data", height=2, width=40, command=self.togglePlayer)
        btn_Roster = tk.Button(sidemenuFrame, text="Get Roster Data", height=2, width=40, command=self.toggleTeam)
        btn_cntPlayer = tk.Button(sidemenuFrame, text="Check Bet", height=2, width=40, command=self.toggleCntPlayer)
        btn_odds = tk.Button(sidemenuFrame, text="Odds", height=2, width=40)
        btn_Exit = tk.Button(sidemenuFrame, text="Exit", height=2, width=40, command=self.callback)

        btn_Player.grid(row=0, column=0, sticky="ew", padx=5, pady=5)
        btn_Roster.grid(row=1, column=0, sticky="ew", padx=5)
        btn_cntPlayer.grid(row=2, column=0, sticky="ew", padx=5, pady=5)
        btn_odds.grid(row=3, column=0, sticky="ew", padx=5)
        btn_Exit.grid(row=4, column=0, sticky="ew", padx=5, pady=5)
        sidemenuFrame.grid_propagate(False)

        Dfont = tk.font.Font(family = "Comic Sans MS",  size = 10) 
        name = tk.Label(sidemenuFrame, text="Made By: Gábor Markó, @mgabor711", bg="grey", font=Dfont)
        sidemenuFrame.rowconfigure(5,weight=2)
        name.grid(row=5, sticky="s")

        self.mainFrame = tk.Frame(master=self.root,width=500,height=600,bg="white")
        self.tableFrame = tk.Frame(master=self.mainFrame, bg="white")

        self.images = []
        self.playerCont = tk.StringVar()
        self.teamCont = tk.StringVar()
        self.selectedCat = tk.StringVar()
        self.overUnder = tk.StringVar()
        self.amount = tk.IntVar()
        self.IDnum = 0
        self.teamCont.set("No Team Selected")
        self.openStatus = 0
        
        self.searchType = 0
        self.seasonType = 'Regular Season'

        self.root.mainloop()

    def initSearch(self):
        self.mainFrame.columnconfigure(0,weight=1, uniform='same')

        self.mainFrame.columnconfigure(1,weight=1, uniform='same')

        self.mainFrame.columnconfigure(2,weight=1, uniform='same')

        self.mainFrame.columnconfigure(3,weight=1, uniform='same')

        self.season = tk.Label(self.mainFrame, text="Season: 2023-24", bg="white", borderwidth=2, relief="solid", pady=10).grid(row=0, sticky="news", columnspan=4)

        self.btn_RegSea = tk.Button(self.mainFrame, text="Regular Season", height=2, bg="lightgreen", command=lambda: self.switchSeasonType(True))
        self.btn_Playoffs = tk.Button(self.mainFrame, text="Playoffs", height=2, bg="grey", command=lambda: self.switchSeasonType(False))

        self.btn_RegSea.grid(row=1, column=0,columnspan=2, sticky="news")
        self.btn_Playoffs.grid(row=1,column=2, columnspan=2, sticky="news")
        
        self.searchType = 0

        self.btn_GameLogs = tk.Button(self.mainFrame, text="Every Game Log", 
        height=2, width=20, bg="lightgreen", command=lambda: self.switchSearchType(0))

        self.btn_LastGame = tk.Button(self.mainFrame, text="Last Game", 
        height=2, width=20, bg="grey", command=lambda: self.switchSearchType(1))

        self.btn_Last5Games = tk.Button(self.mainFrame, text="Last 5 Games", 
        height=2, width=20, bg="grey", command=lambda: self.switchSearchType(5))

        self.btn_Last10Games = tk.Button(self.mainFrame, text="Last 10 Games", 
        height=2, width=20, bg="grey", command=lambda: self.switchSearchType(10))

        self.btn_GameLogs.grid(row=3,column=0, sticky="news")
        self.btn_LastGame.grid(row=3,column=1, sticky="news")
        self.btn_Last5Games.grid(row=3,column=2, sticky="news")
        self.btn_Last10Games.grid(row=3,column=3, sticky="news")
        self.searchBtn = tk.Button(self.mainFrame, text="Search", height=1, width=10, bg="lightblue", command=self.searchResults).grid(row=4, columnspan=4, pady=5)

        self.loading = tk.Label(self.tableFrame, text="Loading", bg="white")
        self.result = 0
        self.player_id = 0
    
    def switchSeasonType(self, bo):
        if bo:
            self.btn_RegSea.config(background="lightgreen")                
            self.btn_Playoffs.config(background="grey")
            self.seasonType = 'Regular Season'

        else:
            self.btn_RegSea.config(background="grey")                
            self.btn_Playoffs.config(background="lightgreen")
            self.seasonType = 'Playoffs'

    def switchSearchType(self, num):
        match num:
            case 0:
                self.btn_GameLogs.config(background="lightgreen")
                self.btn_LastGame.config(background="grey")
                self.btn_Last5Games.config(background="grey")
                self.btn_Last10Games.config(background="grey")

            case 1:
                self.btn_LastGame.config(background="lightgreen")
                self.btn_GameLogs.config(background="grey")
                self.btn_Last5Games.config(background="grey")
                self.btn_Last10Games.config(background="grey")

            case 5:
                self.btn_Last5Games.config(background="lightgreen")
                self.btn_LastGame.config(background="grey")
                self.btn_GameLogs.config(background="grey")
                self.btn_Last10Games.config(background="grey")

            case 10:
                self.btn_Last10Games.config(background="lightgreen")
                self.btn_LastGame.config(background="grey")
                self.btn_GameLogs.config(background="grey")
                self.btn_Last5Games.config(background="grey")
        self.searchType = num
    
    def searchResults(self):
        Dfont = tk.font.Font(family = "Courier New", size = 22, weight = "bold")
        self.loading = tk.Label(self.mainFrame, text="Loading... Please wait.", font=Dfont)
        if(self.openStatus != 3):
            self.loading.grid(row=6, columnspan=4, sticky="news")
        else:
            self.loading.grid(row=7, columnspan=4, sticky="news")
        self.mainFrame.update_idletasks()
        self.tableFrame.destroy()
        self.tableFrame = tk.Frame(master=self.mainFrame, bg="white")
        if self.openStatus == 1:
            self.searchThread = threading.Thread(target=self.searchPlayer(self.searchType, self.seasonType, self.playerCont.get()))
        elif self.openStatus == 2:
            self.searchThread = threading.Thread(target=self.searchTeam(self.searchType, self.seasonType, self.IDnum))
        else:
            self.searchThread = threading.Thread(target=self.searchBet(self.searchType, self.seasonType, self.selectedCat.get(), self.playerCont.get()))

        self.searchThread.start()
    
        self.checkThreadCompletion()

    def checkThreadCompletion(self):
        if self.searchThread.is_alive():
            self.mainFrame.after(100, self.checkThreadCompletion)
        else:
            self.loading.grid_forget()
            self.displayResults()

    def displayResults(self):
        self.gamesPlayed = len(self.result)
        if(self.gamesPlayed == 0):
            Dfont = tk.font.Font(family = "Courier New", size = 22, weight = "bold")
            self.result = tk.Label(self.mainFrame, text="Player did not play in any of the games", font=Dfont)
            if(self.openStatus == 3):
                self.result.grid(row=7, columnspan=4, sticky="news")
            else:
                self.result.grid(row=6, columnspan=4, sticky="news")
            return
        self.table = pt = Table(self.tableFrame, dataframe=self.result.astype(object),
                                #showtoolbar=True, showstatusbar=True
                               )
        pt.show()

        if(self.openStatus == 3):
            self.betResultFrame = tk.Frame(master=self.mainFrame, bg="grey")
            self.hit = 0
            self.avg = 0
            for match in self.result[self.selectedCat.get()]:
                if (match >= float(self.amount.get()) and self.overUnder.get() == 'Over') or (match < float(self.amount.get()) and self.overUnder.get() == 'Under'):
                    self.hit += 1
                self.avg += match
            self.betResult = tk.Label(self.betResultFrame, text="Games played: " + str(self.gamesPlayed)).grid(row=0)
            self.betResult = tk.Label(self.betResultFrame, text="Play hit " + str(self.hit) + " times").grid(row=1)
            self.betResult = tk.Label(self.betResultFrame, text="Average difference: " + str("{:.2f}".format((self.avg - (float(self.amount.get()) * self.gamesPlayed))/self.gamesPlayed))).grid(row=2)
            self.betResult = tk.Label(self.betResultFrame, text=self.selectedCat.get() + " average in these games: " + str("{:.2f}".format(self.avg/self.gamesPlayed))).grid(row=3)
            match self.selectedCat.get():
                case 'AST':
                    metrics = playerestimatedmetrics.PlayerEstimatedMetrics(season="2023-24", season_type=self.seasonType).get_data_frames()[0]
                    metrics = metrics[metrics['PLAYER_ID'] == self.player_id]
                    self.betResult = tk.Label(self.betResultFrame, text="Estimated assist ratio: " + str(metrics['E_AST_RATIO'].iloc[0]) + "%").grid(row=4)
                    self.betResult = tk.Label(self.betResultFrame, text="Estimated metric rank: " + str(metrics['E_AST_RATIO_RANK'].iloc[0])).grid(row=5)
                case 'REB':
                    metrics = playerestimatedmetrics.PlayerEstimatedMetrics(season="2023-24", season_type=self.seasonType).get_data_frames()[0]
                    metrics = metrics[metrics['PLAYER_ID'] == self.player_id]
                    self.betResult = tk.Label(self.betResultFrame, text="Estimated rebound percentage: " + str(metrics['E_REB_PCT'].iloc[0]) + "%").grid(row=4)
                    self.betResult = tk.Label(self.betResultFrame, text="Estimated metric rank: " + str(metrics['E_REB_PCT_RANK'].iloc[0])).grid(row=5)
                case 'FG3M':
                    metrics = playerdashboardbylastngames.PlayerDashboardByLastNGames(season="2023-24", season_type_playoffs=self.seasonType, per_mode_detailed='PerGame', player_id=self.player_id).get_data_frames()[0]
                    self.betResult = tk.Label(self.betResultFrame, text="3pt made rank: " + str(metrics['FG3M_RANK'].iloc[0])).grid(row=4)
                    self.betResult = tk.Label(self.betResultFrame, text="3pt percentage: " + str(metrics['FG3_PCT'].iloc[0]) + "%").grid(row=5)
                    self.betResult = tk.Label(self.betResultFrame, text="3pt percentage rank: " + str(metrics['FG3_PCT_RANK'].iloc[0])).grid(row=6)
                case 'STL':
                    metrics = playerdashboardbylastngames.PlayerDashboardByLastNGames(season="2023-24", season_type_playoffs=self.seasonType, per_mode_detailed='PerGame', player_id=self.player_id).get_data_frames()[0]
                    self.betResult = tk.Label(self.betResultFrame, text="Steals rank: " + str(metrics['STL_RANK'].iloc[0])).grid(row=4)
                case 'BLK':
                    metrics = playerdashboardbylastngames.PlayerDashboardByLastNGames(season="2023-24", season_type_playoffs=self.seasonType, per_mode_detailed='PerGame', player_id=self.player_id).get_data_frames()[0]
                    self.betResult = tk.Label(self.betResultFrame, text="Blocks rank: " + str(metrics['BLK_RANK'].iloc[0])).grid(row=4)
                case 'PTS':
                    metrics = playerdashboardbylastngames.PlayerDashboardByLastNGames(season="2023-24", season_type_playoffs=self.seasonType, per_mode_detailed='PerGame', player_id=self.player_id).get_data_frames()[0]
                    self.betResult = tk.Label(self.betResultFrame, text="Steals rank: " + str(metrics['PTS_RANK'].iloc[0])).grid(row=4)
                
            #+ "\n" + "Play hit " + str(self.hit) + " times"
            #+ "\nSuccess rate: " + str("{:.1f}".format(100 * self.hit/self.gamesPlayed)) + "%"
            #+ "\nAverage difference: " + str("{:.2f}".format((self.avg - (float(self.amount.get()) * self.gamesPlayed))/self.gamesPlayed))
            #+ "\n" + self.selectedCat.get() + " average in these games: " + str("{:.2f}".format(self.avg/self.gamesPlayed)))
            ##+ "\n" + "Estimated pctg: " + metrics['E_AST_RATIO'] + "%"
            ##+ "\n" + "Estimated metric rank: " + metrics['E_AST_RATIO_RANK']

            ## Ideas:
            # DNP
            # Season averages
            self.betResultFrame.grid(row=7, column=0, sticky="news")
            self.tableFrame.grid(row=7, column=1, columnspan=3, sticky="news")
        else:
            self.tableFrame.grid(row=6, columnspan=4, sticky="news")

    def searchPlayer(self, searchType, seasonType, playerCont):
        self.player_id = players.find_players_by_full_name(playerCont)[0]['id']
        if(searchType != 0):
            self.playerdata = playergamelogs.PlayerGameLogs(player_id_nullable=self.player_id,season_nullable='2023-24',season_type_nullable=seasonType,last_n_games_nullable=str(searchType)).get_data_frames()[0].filter(items=['GAME_DATE','MATCHUP','WL','MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS'])
        else:
            self.playerdata = playergamelogs.PlayerGameLogs(player_id_nullable=self.player_id,season_nullable='2023-24',season_type_nullable=seasonType).get_data_frames()[0].filter(items=['GAME_DATE','MATCHUP','WL','MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS','PLUS_MINUS'])
        self.playerdata['GAME_DATE'] = self.playerdata['GAME_DATE'].map(lambda x: str(x)[:-9])
        self.result = self.playerdata
    
    def searchTeam(self, searchType, seasonType, IDnum):
        if searchType != 0:
            self.info = teamplayerdashboard.TeamPlayerDashboard(team_id=teamIDs[IDnum],season='2023-24',per_mode_detailed='PerGame',season_type_all_star=seasonType,last_n_games=str(searchType)).get_data_frames()[1].filter(items=['PLAYER_NAME','GP','GS','MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS']) 
        else:
            self.info = teamplayerdashboard.TeamPlayerDashboard(team_id=teamIDs[IDnum],season='2023-24',per_mode_detailed='PerGame',season_type_all_star=seasonType).get_data_frames()[1].filter(items=['PLAYER_NAME','GP','GS','MIN','FGM','FGA','FG_PCT','FG3M','FG3A','FG3_PCT','FTM','FTA','FT_PCT','OREB','DREB','REB','AST','STL','BLK','TOV','PF','PTS']) 
        self.result = self.info
    
    def searchBet(self, searchType, seasonType, selectedCat, playerCont):
        self.player_id = players.find_players_by_full_name(playerCont)[0]['id']
        if(searchType != 0):
            self.playerdata = playergamelogs.PlayerGameLogs(player_id_nullable=self.player_id,season_nullable='2023-24',season_type_nullable=seasonType,last_n_games_nullable=str(searchType)).get_data_frames()[0].filter(items=['GAME_DATE','MATCHUP',selectedCat])
        else:
            self.playerdata = playergamelogs.PlayerGameLogs(player_id_nullable=self.player_id,season_nullable='2023-24',season_type_nullable=seasonType).get_data_frames()[0].filter(items=['GAME_DATE','MATCHUP',selectedCat])
        self.playerdata['GAME_DATE'] = self.playerdata['GAME_DATE'].map(lambda x: str(x)[:-9])
        self.result = self.playerdata
    
    def togglePlayer(self):
        if self.openStatus == 1:
            self.openStatus = 0
            self.mainFrame.pack_forget()
            return
        for wid in self.mainFrame.grid_slaves():
            wid.grid_forget()
        self.mainFrame.rowconfigure(6, weight=1)
        self.mainFrame.rowconfigure(7, weight=0)
        self.initSearch()
        self.player = tk.Label(self.mainFrame, text="Player:", bg="white").grid(row=2,column=0, sticky="news", pady=10)
        self.modify = tk.Entry(self.mainFrame, textvariable=self.playerCont, bg="lightblue").grid(row=2,column=1, sticky="news", columnspan=3, pady=5, padx=5)
        self.mainFrame.pack(fill=tk.BOTH, side=tk.TOP,expand=True)
        self.openStatus = 1

    def toggleTeam(self):
        self.images = []
        if self.openStatus == 2:
            self.openStatus = 0
            self.mainFrame.pack_forget()
            return
        for wid in self.mainFrame.grid_slaves():
            wid.grid_forget()
        self.mainFrame.rowconfigure(6, weight=1)
        self.mainFrame.rowconfigure(7, weight=0)
        self.initSearch()
        self.team = tk.Label(self.mainFrame, text="Select team:", bg="white").grid(row=2,column=0, sticky="news", pady=10)
        self.teamBtn = tk.Button(self.mainFrame, textvariable=self.teamCont, bg="white", command=self.pickTeam).grid(row=2, column=1, pady=5)
        self.mainFrame.pack(fill=tk.BOTH, side=tk.LEFT,expand=True)
        self.openStatus = 2
    
    def toggleCntPlayer(self):
        for wid in self.mainFrame.grid_slaves():
            wid.grid_forget()
        if self.openStatus == 3:
            self.overUnderLabel.grid_remove()
            self.amount.grid_remove()
            self.openStatus = 0
            self.mainFrame.pack_forget()
            return
        self.initSearch()
        self.mainFrame.rowconfigure(6, weight=0)
        self.mainFrame.rowconfigure(7, weight=1)
        self.player = tk.Label(self.mainFrame, text="Player:", bg="white").grid(row=2,column=0, sticky="news", pady=5)
        self.modify = tk.Entry(self.mainFrame, textvariable=self.playerCont, bg="lightblue").grid(row=2,column=1, sticky="news", pady=5, padx=5)

        self.statCatLabel = tk.Label(self.mainFrame, text="Category:", bg="white").grid(row=2,column=2, sticky="news", pady=5)
        self.options = ['FG3M','REB','AST','STL','BLK','PTS']
        self.category = tk.OptionMenu(self.mainFrame, self.selectedCat, *self.options).grid(row=2,column=3, sticky="news", pady=5, padx=5)

        self.ou = ['Over','Under']
        self.overUnderLabel = tk.OptionMenu(self.mainFrame, self.overUnder, *self.ou)
        self.overUnderLabel.grid(row=6,column=1, sticky="news", pady=5)
        #self.overUnderLabel = tk.Label(self.mainFrame, text="Over/Under", bg="white").grid(row=6,column=1, sticky="news", pady=5)
        #self.amount = tk.Entry(self.mainFrame, textvariable=self.amountCont, bg="lightblue").grid(row=6,column=2, sticky="news", pady=5, padx=5)
        self.amount = tk.Spinbox(self.mainFrame, from_=0.5, to=35.5, increment=1, format="%.1f", state='readonly')
        self.amount.grid(row=6,column=2, sticky="news", pady=5, padx=5)

        self.mainFrame.pack(fill=tk.BOTH, side=tk.LEFT,expand=True)
        self.openStatus = 3

    def pickTeam(self):
        self.openStatus = 0
        for wid in self.mainFrame.grid_slaves():
            wid.grid_forget()
        self.photos = []
        self.buttons = []
        window_width = self.mainFrame.winfo_width()
        window_height = self.mainFrame.winfo_height()
        for i in range(6):
            self.mainFrame.rowconfigure(i, weight=1, uniform='same')  
            for j in range(5):
                self.mainFrame.columnconfigure(j, weight=1, uniform='same')
                self.image = Image.open('images/' + teamsCont[i*5+j].replace(" ","") +'.png')
                self.images.append(self.image)
                perc_minus = 0.2 - round(window_height/self.root.winfo_screenheight()/5,2)
                self.photo = ImageTk.PhotoImage(self.image.resize((int(window_width // 6 * (0.76-perc_minus)), int(window_height // 5 * (0.76-perc_minus)))))
                self.photos.append(self.photo)
                self.bf = tk.font.Font(family="Times New Roman", size=11, weight="bold")
                self.button = tk.Button(self.mainFrame, text=teamsCont[i*5+j], font=self.bf,
                               image=self.photos[-1], compound=tk.TOP,
                               command=lambda arg1=teamsCont[i*5+j], arg2=(i*5+j): self.selectTeam(arg1, arg2))

                self.button.grid(row=i, column=j, sticky="news", columnspan=1, rowspan=1)
                self.buttons.append(self.button)
        self.mainFrame.bind("<Configure>", self.resizeImages)
    
    def resizeImages(self, event):
        window_width = self.mainFrame.winfo_width()
        window_height = self.mainFrame.winfo_height()

        perc_minus = 0.2 - round(window_height/self.root.winfo_screenheight()/5,2) #float("{:.2f}".format(

        cell_width = int(window_width // 6 *(0.76-perc_minus))
        cell_height = int(window_height // 5*(0.76-perc_minus))

        for i in range(30):
            resized_image = self.images[i].resize((cell_width, cell_height))
            self.photo = ImageTk.PhotoImage(resized_image)
            self.buttons[i].config(image=self.photo)
            self.buttons[i].image = self.photo
    
    def selectTeam(self,newtext, num):
        self.tableFrame.destroy()
        self.mainFrame.destroy()     
        self.mainFrame = tk.Frame(master=self.root,width=500,height=600,bg="white")
        self.tableFrame = tk.Frame(master=self.mainFrame, bg="white")
        self.teamCont.set(newtext)
        self.IDnum = num
        self.toggleTeam()

if __name__ == "__main__":
    app = App()
