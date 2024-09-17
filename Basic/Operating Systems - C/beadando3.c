#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdlib.h>
#include <time.h>
#include <ctype.h>
#include <unistd.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/wait.h>
#include <fcntl.h>

#define MAX_WORKERS 10
#define MAX_DAYS 7
#define MAX_NAME_LENGTH 50
#define BUS_CAPACITY 5

// A beadandó az opsys.inf.elte.hu kiszolgálón lett tesztelve

void addApplicants(char (*days)[MAX_DAYS][MAX_WORKERS][MAX_NAME_LENGTH], int (*num_workers)[MAX_DAYS]) {
  char name[MAX_NAME_LENGTH+1];
  char response[2*MAX_NAME_LENGTH+1];
  char temp;
  bool done = false;
  bool wasntThereBefore = true;
  printf("--------------------------------------------\n");
  printf("Kérjük adja meg az elérhető munkások nevét.\n");
  printf("Írja be az 'OK' kifejezést ha végzett.\n");
  printf("--------------------------------------------\n");
  scanf("%c",&temp);
  while (!done) {
    printf("Írja be a dolgozó nevét (vagy 'OK'): ");
    fgets(name,MAX_NAME_LENGTH+1,stdin);
    name[strcspn(name, "\n")] = 0;
    int ctr = 0;
    if(strlen(name) == 50) {
      int c; while ((c = fgetc(stdin)) != EOF && c != '\n') {ctr++;}
    }
    if(ctr > 0) {
      printf("Kérem tartsa be a maximális karakterlimitet! (50)\n");
      strcpy(name,"");
    }
    else {
      if (strcmp(name, "OK") == 0) {
        done = 1;
      } else {
        printf("Mely napokon tud %s dolgozni? (pl. 'Hétfő Szerda Péntek'): ", name);
        fgets(response,2*MAX_NAME_LENGTH+1,stdin);
        response[strcspn(response, "\n")] = 0;
        ctr = 0;
        if(strlen(response) == 100) {
          int c; while ((c = fgetc(stdin)) != EOF && c != '\n') {ctr++;}
        }
        if(ctr > 0) {
          printf("Kérem tartsa be a maximális karakterlimitet! (100)\n");
          strcpy(response,"");
        }
        char *day = strtok(response, " ");
        while (day != NULL) {
          char *temp2 = day;
          int i = 0;
          while(day[i]) {
            temp2[i] = tolower(day[i]);
            i++;
          }
          int day_index;
          if (strcmp(temp2, "hétfő") == 0) {
            day_index = 0;
          } else if (strcmp(temp2, "kedd") == 0) {
            day_index = 1;
          } else if (strcmp(temp2, "szerda") == 0) {
            day_index = 2;
          } else if (strcmp(temp2, "csütörtök") == 0) {
            day_index = 3;
          } else if (strcmp(temp2, "péntek") == 0) {
            day_index = 4;
          } else if (strcmp(temp2, "szombat") == 0) {
            day_index = 5;
          } else if (strcmp(temp2, "vasárnap") == 0) {
            day_index = 6;
          } else {
            printf("Nem létezik ilyen nap: %s\n", day);
            day = strtok(NULL, " ");
            continue;
          }
          if ((*num_workers)[day_index]+1 >= MAX_WORKERS) {
            printf("%s már nincs több hely.\n", temp2);
          } else {
            for(int i = 0; i < (*num_workers)[day_index]; i++) {
              if(strcmp((*days)[day_index][i], name) == 0) {
                wasntThereBefore = false;
              }
            }
            if(wasntThereBefore == true) {
              strcpy((*days)[day_index][(*num_workers)[day_index]], name);
              (*num_workers)[day_index]++;
            }
          }
          wasntThereBefore = true;
          day = strtok(NULL, " ");
        }
        strcpy(name,"");
      }
    printf("--------------------------------------------\n");
    }
  }
}

void deleteApplicants(char (*days)[MAX_DAYS][MAX_WORKERS][MAX_NAME_LENGTH], int (*num_workers)[MAX_DAYS]) {
    char name[MAX_NAME_LENGTH];
    char temp;
    printf("--------------------------------------------\n");
    printf("Add meg a jelentkező nevét, akinek az adatait törölni szeretnéd: \n");
    scanf("%c",&temp);
    scanf("%[^\n]s",&name);
    bool found = false;
    for(int i = 0; i < MAX_DAYS; i++) {
        for(int j = 0; j < (*num_workers)[i]; j++) {
          if(strcmp((*days)[i][j], name) == 0) {
            found = true;
            strcpy((*days)[i][j], "");
            for(int k = j+1;k < (*num_workers)[i];k++) {
              strcpy((*days)[i][k-1], (*days)[i][k]);
            }
            (*num_workers)[i]--;
          }
        }
      }
    printf("--------------------------------------------\n");
    if (found) {
        printf("A jelentkező adatai sikeresen törölve lettek.\n");
    } else {
        printf("A megadott névvel nem található jelentkező.\n");
    }
    printf("--------------------------------------------\n");
}

void printApplicants(char (*days)[MAX_DAYS][MAX_WORKERS][MAX_NAME_LENGTH], int (*num_workers)[MAX_DAYS], char (*name_of_days)[MAX_DAYS][MAX_NAME_LENGTH]) {
    int print_response = 99;
    char tempp;
    char response[MAX_NAME_LENGTH+1];
    printf("--------------------------------------------\n");
    printf("Válasszon listázási típust: \n");
    printf("1 : Napi\n");
    printf("2 : Teljes\n");
    if(scanf("%d", &print_response) != 1) {    
      int c; while ((c = fgetc(stdin)) != EOF && c != '\n') {}
    }
    printf("--------------------------------------------\n");
    if(print_response == 1) {
      printf("Mely napot szeretné látni? (pl. 'Hétfő'):\n");
      scanf("%c",&tempp);
      fgets(response,2*MAX_NAME_LENGTH+1,stdin);
      response[strcspn(response, "\n")] = 0;
      int ctr = 0;
      if(strlen(response) == 50) {
        int c; while ((c = fgetc(stdin)) != EOF && c != '\n') {ctr++;}
      }
      if(ctr > 0) {
        printf("Kérem tartsa be a maximális karakterlimitet! (50)\n");
        strcpy(response,"");
      }
      char *day = strtok(response, " ");
	    printf("--------------------------------------------\n");
        while (day != NULL) {
          char *temp = day;
          int i = 0;
          while(day[i]) {
            temp[i] = tolower(day[i]);
            i++;
          }
          int day_index = 99;
          if (strcmp(temp, "hétfő") == 0) {
            day_index = 0;
          } else if (strcmp(temp, "kedd") == 0) {
            day_index = 1;
          } else if (strcmp(temp, "szerda") == 0) {
            day_index = 2;
          } else if (strcmp(temp, "csütörtök") == 0) {
            day_index = 3;
          } else if (strcmp(temp, "péntek") == 0) {
            day_index = 4;
          } else if (strcmp(temp, "szombat") == 0) {
            day_index = 5;
          } else if (strcmp(temp, "vasárnap") == 0) {
            day_index = 6;
          } else {
            printf("Nem létezik ilyen nap: %s\n", day);
            day = strtok(NULL, " ");
            continue;
          }
          day = strtok(NULL, " ");
          if (day_index != 99) {
            printf("%s:\n", (*name_of_days)[day_index]);
            for(int i = 0; i < (*num_workers)[day_index]; i++) {
                printf("%s\n", (*days)[day_index][i]);
            }
            printf("--------------------------------------------\n");
        }
        }
      }
    else if(print_response == 2) {
      for(int i = 0; i < MAX_DAYS; i++) {
        printf("%s:\n", (*name_of_days)[i]);
        for(int j = 0; j < (*num_workers)[i]; j++) {
          printf("%s\n", (*days)[i][j]);
        }
        printf("--------------------------------------------\n");
      }
    } else {
        printf("Kérem válasszon a lehetőségek közül!\n");
    }
}

void saveApplicants(char (*days)[MAX_DAYS][MAX_WORKERS][MAX_NAME_LENGTH], int (*num_workers)[MAX_DAYS]) {
  int file = open("applicants_data.bin", O_WRONLY | O_CREAT | O_TRUNC, 0777);

  if (file == -1) {
    printf("--------------------------------------------\n\n");
    printf("Nem található ilyen fájl!\n\n"); 
    printf("--------------------------------------------\n\n");
  }
  else {
    for(int i = 0; i < MAX_DAYS; i++) {
      write(file, &(*num_workers)[i], sizeof(int));
    }
    for(int i = 0; i < MAX_DAYS; i++) {
        for(int j = 0; j < (*num_workers)[i]; j++) {
            int length = sizeof((*days)[i][j])/sizeof((*days)[i][j][0]);
            write(file, &(*days)[i][j], length * sizeof(char));
        }
      }
    printf("--------------------------------------------\n\n");
    printf("A mentés sikeresen megtörtént.\n\n");
    printf("--------------------------------------------\n\n");
  }

  close(file);
}

void loadApplicants(char (*days)[MAX_DAYS][MAX_WORKERS][MAX_NAME_LENGTH], int (*num_workers)[MAX_DAYS]) {
    int file = open("applicants_data.bin", O_RDONLY | O_CREAT, 0777);
    if (file == -1) {
      printf("--------------------------------------------\n\n");
      printf("Hiba történt!\n\n");
      printf("--------------------------------------------\n\n");
    }
    else{
      for (int i = 0; i < MAX_DAYS; i++){
        read(file, &(*num_workers)[i], sizeof(int));
      }
      for(int i = 0; i < MAX_DAYS; i++) {
        for(int j = 0; j < (*num_workers)[i]; j++) {
          int length = sizeof((*days)[i][j])/sizeof((*days)[i][j][0]);
            read(file, &(*days)[i][j], length * sizeof(char));
        }
      }
      printf("--------------------------------------------\n\n");
      printf("A betöltés sikeresen megtörtént.\n\n");
      printf("--------------------------------------------\n\n"); 
    }

    close(file);
}

void modifyApplicants(char (*days)[MAX_DAYS][MAX_WORKERS][MAX_NAME_LENGTH], int (*num_workers)[MAX_DAYS]) {
    char name[MAX_NAME_LENGTH+1];
    char name2[MAX_NAME_LENGTH+1];
    char response[2*MAX_NAME_LENGTH+1];
    char temp;
    int modify_response = 99;
    printf("--------------------------------------------\n");
    printf("Add meg a jelentkező nevét, akinek az adatait módosítani szeretnéd: \n");
    scanf("%c",&temp);
    fgets(name,MAX_NAME_LENGTH+1,stdin);
    name[strcspn(name, "\n")] = 0;
    int ctr = 0;
    if(strlen(name) == 50) {
      int c; while ((c = fgetc(stdin)) != EOF && c != '\n') {ctr++;}
    }
    if(ctr > 0) {
      printf("Kérem tartsa be a maximális karakterlimitet! (50)\n");
      strcpy(name,"");
    }
    bool found = false;
    for(int i = 0; i < MAX_DAYS; i++) {
        for(int j = 0; j < (*num_workers)[i]; j++) {
          if(strcmp((*days)[i][j], name) == 0) {
            found = true;
          }
        }
      }
    if(found) {
    while(true){
      printf("--------------------------------------------\n");
      printf("Mit szeretne módosítani?\n");
      printf("1 : Név\n");
      printf("2 : Munkanapok\n");
      printf("3 : Kilépés\n");
      printf("--------------------------------------------\n");
      if(scanf("%d",&modify_response) != 1) {
        int c; while ((c = fgetc(stdin)) != EOF && c != '\n') {}
      }
      if(modify_response == 1) {
        printf("Mi legyen az új név?\n");
        scanf("%c",&temp);
        fgets(name2,MAX_NAME_LENGTH+1,stdin);
        name2[strcspn(name2, "\n")] = 0;
        int ctr = 0;
        if(strlen(name2) == 50) {
          int c; while ((c = fgetc(stdin)) != EOF && c != '\n') {ctr++;}
        }
        if(ctr > 0) {
          printf("Kérem tartsa be a maximális karakterlimitet! (50)\n");
          strcpy(name2,"");
        }
        else {
        for(int i = 0; i < MAX_DAYS; i++) {
          for(int j = 0; j < (*num_workers)[i]; j++) {
            if(strcmp((*days)[i][j],name) == 0) {
              strcpy((*days)[i][j], name2);
            }
          }
        }
        strcpy(name,name2);
        }
      }
      else if(modify_response == 2) {
        printf("Mely munkanapok kerüljenek be az adatok közé?\n");
        scanf("%c",&temp);
        fgets(response,2*MAX_NAME_LENGTH+1,stdin);
        response[strcspn(response, "\n")] = 0;
        ctr = 0;
        if(strlen(response) == 100) {
          int c; while ((c = fgetc(stdin)) != EOF && c != '\n') {ctr++;}
        }
        if(ctr > 0) {
          printf("Kérem tartsa be a maximális karakterlimitet! (100)\n");
          strcpy(response,"");
        }
        else {
          for(int i = 0; i < MAX_DAYS; i++) {
            for(int j = 0; j < (*num_workers)[i]; j++) {
              if(strcmp((*days)[i][j],name) == 0) {
                strcpy((*days)[i][j],"");
                for(int k = j+1;k < (*num_workers)[i];k++) {
                  strcpy((*days)[i][k-1], (*days)[i][k]);
                }
                (*num_workers)[i]--;
              }
            }
          }
        }
        char *day = strtok(response, " ");
        while (day != NULL) {
          char *temp2 = day;
          int i = 0;
          while(day[i]) {
            temp2[i] = tolower(day[i]);
            i++;
          }
          int day_index = 99;
          if (strcmp(temp2, "hétfő") == 0) {
            day_index = 0;
          } else if (strcmp(temp2, "kedd") == 0) {
            day_index = 1;
          } else if (strcmp(temp2, "szerda") == 0) {
            day_index = 2;
          } else if (strcmp(temp2, "csütörtök") == 0) {
            day_index = 3;
          } else if (strcmp(temp2, "péntek") == 0) {
            day_index = 4;
          } else if (strcmp(temp2, "szombat") == 0) {
            day_index = 5;
          } else if (strcmp(temp2, "vasárnap") == 0) {
            day_index = 6;
          } else {
            printf("Nem létezik ilyen nap: %s\n", day);
            day = strtok(NULL, " ");
            continue;
          }
          if ((*num_workers)[day_index]+1 >= MAX_WORKERS) {
            printf("%s már nincs több hely.\n", temp2);
          } else {
            strcpy((*days)[day_index][(*num_workers)[day_index]], name);
            (*num_workers)[day_index]++;
          }
          day = strtok(NULL, " ");
        }
      }
      else if(modify_response == 3) {
        printf("A jelentkező adatai sikeresen módosítva lettek.\n");
        return;
      }
      else {
        printf("Kérem válasszon a lehetőségek közül!\n");
      }
    }
    } else {
        printf("A megadott névvel nem található jelentkező.\n");
    }
    printf("--------------------------------------------\n");
}

struct uzenet {
  long mtype;
  char busData[BUS_CAPACITY][MAX_NAME_LENGTH];
};

void handler(int sig_num) {
  if(sig_num == SIGUSR1) {
    //Szignál teszt
    //printf("Szignál a gyerekfolyamat felől a szülő felé\n");
    return;
  }
  else if (sig_num == SIGUSR2) {
    //Szignál teszt
    //printf("Szignál a szülő felől a gyerekfolyamat felé\n");
    return;
  }
}

void bringWorkers(int currentDay, key_t key, char (*days)[MAX_DAYS][MAX_WORKERS][MAX_NAME_LENGTH], int (*num_workers)[MAX_DAYS]) {
  int pipefd1[2], pipefd2[2], status, uzenetsor;
  srand(time(NULL));

  if(pipe(pipefd1) == -1 || pipe(pipefd2) == -1){
    perror("Hiba a pipe nyitásakor!\n");
    exit(EXIT_FAILURE);
  }

  uzenetsor = msgget(key, 0600 | IPC_CREAT);
  if(uzenetsor < 0) {
    perror("Hiba az üzenetsor nyitásakor!\n");
    exit(EXIT_FAILURE);
  }

  struct sigaction sig = {0};
  sig.sa_handler = &handler;
  sigemptyset(&sig.sa_mask);
  sigfillset(&sig.sa_mask);
  sigdelset(&sig.sa_mask,SIGUSR1);
  sigdelset(&sig.sa_mask,SIGUSR2);
  sigprocmask(SIG_BLOCK,&sig.sa_mask,NULL);
  sig.sa_flags = SA_RESTART;
  sigaction(SIGUSR1,&sig,NULL);
  sigaction(SIGUSR2,&sig,NULL);

  pid_t process, process2;
  process = fork();

  if(process < 0){
    perror("Fork hiba\n");
    exit(EXIT_FAILURE);
  }
  if(process == 0) {
    char busOne[BUS_CAPACITY][MAX_NAME_LENGTH];
    int readOne = 0;
    printf("--------------------------------------------\n");
    printf("A BUSZ-1 készen áll az indulásra\n");
    printf("--------------------------------------------\n");
    kill(getppid(), SIGUSR1);
    sigsuspend(&sig.sa_mask);
    printf("--------------------------------------------\n");
    printf("A BUSZ-1 névsora:\n");
    close(pipefd1[1]);
    for(int j = 0; j < (*num_workers)[currentDay]; j++){
      if(j < BUS_CAPACITY) {
        read(pipefd1[0],busOne[j],MAX_NAME_LENGTH * sizeof(char));
        printf("%s\n", busOne[j]);
        readOne++;
      }
    }
    printf("--------------------------------------------\n");
    if(readOne == 0){
      printf("--------------------------------------------\n");
      printf("BUSZ-1 nem indult el utasok hiányában\n");
      printf("--------------------------------------------\n");
      kill(getppid(), SIGUSR1);
      struct uzenet uz;
      uz.mtype = 5;
      strcpy(uz.busData[0], "");
      status = msgsnd(uzenetsor,&uz,BUS_CAPACITY * MAX_NAME_LENGTH * sizeof(char),0);
      if(status < 0) {
        perror("Üzenetküldési hiba\n");
        exit(EXIT_FAILURE);
      }
    }   
    else{
      printf("--------------------------------------------\n");
      printf("A BUSZ-1 úton van!\n");
      kill(getppid(), SIGUSR1);
      sigsuspend(&sig.sa_mask);
      for(int i = 0;i < readOne;i++){
        int chance = rand() % 101;
        if(chance < 10) {
          printf("%s nem szállt fel a buszra\n", busOne[i]);
          strcpy(busOne[i], "-");
        }
      }
      printf("--------------------------------------------\n"); 
      struct uzenet uz;
      uz.mtype = 5;
      for(int k = 0; k < BUS_CAPACITY; k++) {
        if(k < readOne){
          strcpy(uz.busData[k], busOne[k]);
        }
        else {
          strcpy(uz.busData[k], "");
        }
      }
      status = msgsnd(uzenetsor,&uz,BUS_CAPACITY * MAX_NAME_LENGTH * sizeof(char),0);
      if(status < 0) {
        perror("Üzenetküldési hiba\n");
        exit(EXIT_FAILURE);
      }
    }
    close(pipefd1[0]);
    exit(0);
  } else {

    sigsuspend(&sig.sa_mask);

    process2 = fork();
    
    if(process2 < 0){
      perror("Fork hiba\n");
      exit(EXIT_FAILURE);
    }
    if(process2 == 0){
      char busTwo[BUS_CAPACITY][MAX_NAME_LENGTH];
      int readTwo = 0;
      printf("--------------------------------------------\n");
      printf("A BUSZ-2 készen áll az indulásra\n");
      printf("--------------------------------------------\n");
      kill(getppid(), SIGUSR1);
      sigsuspend(&sig.sa_mask);
      close(pipefd2[1]);
      printf("--------------------------------------------\n");
      printf("A BUSZ-2 névsora:\n");
      for(int j = 0; j < (*num_workers)[currentDay]-BUS_CAPACITY; j++) {
        read(pipefd2[0],busTwo[j],MAX_NAME_LENGTH * sizeof(char));
        printf("%s\n", busTwo[j]);
        readTwo++;
      }
      printf("--------------------------------------------\n");
      close(pipefd2[0]);
      if(readTwo == 0) {
        printf("--------------------------------------------\n");
        printf("BUSZ-2 nem indult el utasok hiányában\n");
        printf("--------------------------------------------\n");
        kill(getppid(), SIGUSR1);
      }
      else {
        printf("--------------------------------------------\n");
        printf("A BUSZ-2 úton van!\n");
        kill(getppid(), SIGUSR1);
        sigsuspend(&sig.sa_mask);
        for(int i = 0;i < readTwo;i++){
          int chance = rand() % 101;
          if(chance < 10) {
            printf("%s nem szállt fel a buszra\n", busTwo[i]);
            strcpy(busTwo[i], "-");
          }
        }
        printf("--------------------------------------------\n");
        struct uzenet uz2;
        uz2.mtype = 6;
        for(int k = 0; k < BUS_CAPACITY; k++) {
          if(k < readTwo){
            strcpy(uz2.busData[k], busTwo[k]);
          }
          else {
            strcpy(uz2.busData[k], "");
          }
        }
        status = msgsnd(uzenetsor,&uz2,BUS_CAPACITY * MAX_NAME_LENGTH * sizeof(char),0);
        if(status < 0) {
          perror("Üzenetküldési hiba\n");
          exit(EXIT_FAILURE);
        } 
      }
      
      exit(0);
    }
    else {
      sigsuspend(&sig.sa_mask);
      int workerCtr = 0;
      close(pipefd1[0]);
      close(pipefd2[0]);
      for(int i = 0;i < (*num_workers)[currentDay];i++){
        if(i < BUS_CAPACITY) {
          write(pipefd1[1],(*days)[currentDay][i],MAX_NAME_LENGTH * sizeof(char));
        }
        else {
          write(pipefd2[1],(*days)[currentDay][i],MAX_NAME_LENGTH * sizeof(char));
        }
      }
      close(pipefd1[1]);
      close(pipefd2[1]);

      kill(process,SIGUSR2);
      sigsuspend(&sig.sa_mask);
      kill(process2,SIGUSR2);
      sigsuspend(&sig.sa_mask);
      kill(process,SIGUSR2);
      kill(process2,SIGUSR2);

      struct uzenet uz;
      int status = msgrcv(uzenetsor,&uz,BUS_CAPACITY * MAX_NAME_LENGTH * sizeof(char),5,0);
      if(status < 0) {
        perror("Üzenetfogadási hiba\n");
        exit(EXIT_FAILURE);
      }
      if(uz.busData[0][0] != '\0') {
        printf("BUSZ-1 megérkezett:\n");
        for(int i = 0; i < BUS_CAPACITY;i++) {
          if(uz.busData[i][0] != '\0') {
            printf("%s\n", uz.busData[i]);
            if(strcmp(uz.busData[i],"-") != 0) {
              workerCtr++;
            }
          }
          else {
            break;
          }
        }
        printf("--------------------------------------------\n");
        if(uz.busData[4][0] != '\0') {
          struct uzenet uz2;
          int status = msgrcv(uzenetsor,&uz2,BUS_CAPACITY * MAX_NAME_LENGTH * sizeof(char),6,0);
          if(status < 0) {
            perror("Üzenetfogadási hiba\n");
            exit(EXIT_FAILURE);
          }
          for(int i = 0; i < BUS_CAPACITY;i++) {
            if(uz2.busData[i][0] != '\0') {
              if(i == 0) {printf("BUSZ-2 megérkezett:\n");}
              printf("%s\n", uz2.busData[i]);
              if(strcmp(uz2.busData[i],"-") != 0) {
                workerCtr++;
              }
            }
            else {
              break;
            }
          }
          printf("--------------------------------------------\n");
        }
      }
      printf("Összesen %i munkás érkezett meg\n", workerCtr);
      printf("--------------------------------------------\n");
      int deadChildren = 0;
      while(deadChildren != 2) {
        wait(NULL);
        deadChildren++;
      }
      status = msgctl(uzenetsor,IPC_RMID,NULL);
      if(status < 0) {
        perror("Hiba az üzenetsor eltávolításában\n");
        exit(EXIT_FAILURE);
      }
      fflush(NULL);
    }
  }
}

int main(int argc, char* argv[]) {
  char days[MAX_DAYS][MAX_WORKERS][MAX_NAME_LENGTH];
  int num_workers[MAX_DAYS] = {0};
  char name_of_days[MAX_DAYS][MAX_NAME_LENGTH] = {"Hétfő","Kedd","Szerda","Csütörtök","Péntek","Szombat","Vasárnap"};
  int menu_response = 99;
  int quit_response = 99;
  int active_day = 0;
  key_t key = ftok(argv[0],1);

  printf("Üdvözöljük az 'Igyál komám' pincészet dolgozói regisztrációs felületén.\n");

  while(true) {
    printf("--------------------------------------------\n");
    printf("A mai nap: %s \n", name_of_days[active_day]);
    printf("--------------------------------------------\n");
    printf("Kérjük válasszon az alábbi menüből:\n");
    printf("0 : Kilépés\n");
    printf("1 : Dolgozó hozzáadása\n");
    printf("2 : Dolgozó eltávolítása\n");
    printf("3 : Dolgozók kiiratása\n");
    printf("4 : Dolgozó adatainak módosítása\n");
    printf("5 : Adatok mentése\n");
    printf("6 : Adatok betöltése\n");
    printf("7 : - Ugrás a következő napra -\n");
    while(scanf("%d", &menu_response) != 1) {
      printf("--------------------------------------------\n");
      printf("Nem számot adott meg!\n");
      printf("--------------------------------------------\n");
      printf("Kérjük válasszon az alábbi menüből:\n");
      printf("0 : Kilépés\n");
      printf("1 : Dolgozó hozzáadása\n");
      printf("2 : Dolgozó eltávolítása\n");
      printf("3 : Dolgozók kiiratása\n");
      printf("4 : Dolgozó adatainak módosítása\n");
      printf("5 : Adatok mentése\n");
      printf("6 : Adatok betöltése\n");
      printf("7 : - Ugrás a következő napra -\n");
      int c; while ((c = fgetc(stdin)) != EOF && c != '\n') {}
    }

    if (menu_response == 0) {
      while(true) {
        printf("Szeretne-e menteni kilépés előtt? (Mentés nélkül a munkamenet adatai elvesznek)\n");
        printf("1 : Igen (Mentés és Kilépés)\n");
        printf("2 : Nem (Nincs mentés)\n");
        while(scanf("%d",&quit_response) != 1) {
          printf("--------------------------------------------\n");
          printf("Nem számot adott meg!\n");
          printf("--------------------------------------------\n");
          printf("Szeretne-e menteni kilépés előtt? (Mentés nélkül a munkamenet adatai elvesznek)\n");
          printf("1 : Igen (Mentés és Kilépés)\n");
          printf("2 : Nem (Nincs mentés)\n");
          int c; while ((c = fgetc(stdin)) != EOF && c != '\n') {}
        }

        if(quit_response == 1) {
          saveApplicants(&days,&num_workers);
          return 0;
        }
        else if (quit_response == 2) {
          return 0;
        }
        else {
          printf("--------------------------------------------\n");
          printf("Kérem válasszon a menüből!\n");
          printf("--------------------------------------------\n");
        }
      }
    }
    else if (menu_response == 1) {
      addApplicants(&days,&num_workers);
    }
    else if (menu_response == 2) {
      deleteApplicants(&days,&num_workers);
    }
    else if (menu_response == 3) {
      printApplicants(&days,&num_workers,&name_of_days);
    }
    else if (menu_response == 4) {
      modifyApplicants(&days,&num_workers);
    }
    else if (menu_response == 5) {
      saveApplicants(&days,&num_workers);
    }
    else if (menu_response == 6) {
      loadApplicants(&days,&num_workers);
    }
    else if (menu_response == 7) {
      if(active_day == 6) {
        active_day = 0;
      } else {
        active_day++;
      }
      bringWorkers(active_day,key,&days,&num_workers);
    }
    else {
      printf("--------------------------------------------\n");
      printf("Kérem válasszon a menüből!\n");
      printf("--------------------------------------------\n");
    }
  }

  return 0;
}