#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdlib.h>
#include <ctype.h>
#include <unistd.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/wait.h>
#include <fcntl.h>

void handler(int sig_num) {
  if(sig_num == SIGUSR1) {
    //Szignál teszt
    printf("Szignál az 1. gyerekfolyamat felől a szülő felé\n");
    return;
  }
  else if (sig_num == SIGUSR2) {
    //Szignál teszt
    printf("Szignál az 2. gyerekfolyamat felől a szülő felé\n");
    return;
  }
  else if (sig_num == SIGINT) {
    //Szignál teszt
    printf("Szignál a szülő felől a gyerek felé\n");
    return;
  }
}

struct uzenet {
  long mtype;
  char text[1030];
};

void processKidsAndTeacher(key_t key, char arg1[1024], char arg2[1024]) {
  int pipefd1[2], pipefd2[2], status, uzenetsor;

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
  sigdelset(&sig.sa_mask,SIGINT);
  sigprocmask(SIG_BLOCK,&sig.sa_mask,NULL);
  sig.sa_flags = SA_RESTART;
  sigaction(SIGUSR1,&sig,NULL);
  sigaction(SIGUSR2,&sig,NULL);
  sigaction(SIGINT,&sig,NULL);

  pid_t process, process2;
  process = fork();

  char hely[1024];
  char ido[5];

  struct uzenet uzi;

  if(process < 0){
    perror("Fork hiba\n");
    exit(EXIT_FAILURE);
  }
  if(process == 0) {
    printf("1.gyerek elindul az útjára\n");
    kill(getppid(), SIGUSR1);
    sigsuspend(&sig.sa_mask);
    close(pipefd1[1]);
    read(pipefd1[0],hely,1024);
    read(pipefd1[0],ido,5);
    close(pipefd1[0]);
    printf("Gyerek 1 hamarabb érkezett és a tüntetés miatt más helyszínt jelöl ki\n");
    struct uzenet uzi2;
    uzi.mtype = 5;
    uzi2.mtype = 6;
    strcpy(uzi.text, "Trafalgar Tér;18");
    strcpy(uzi2.text, "Trafalgar Tér;18");
    strcpy(hely, "Trafalgar Tér");
    strcpy(ido, "18");
    status = msgsnd(uzenetsor,&uzi,1030,0);
    status = msgsnd(uzenetsor,&uzi2,1030,0);
    printf("Gyerek 1 megérkezetett ide: %s, időpont: %s\n",hely, ido);
    exit(0);
  } else {
    process2 = fork();
    if(process2 == 0) {
        sigsuspend(&sig.sa_mask);
        printf("2.gyerek elindul az útjára\n");
        kill(getppid(), SIGUSR2);
        sigsuspend(&sig.sa_mask);
        close(pipefd2[1]);
        read(pipefd2[0],hely,1024);
        read(pipefd2[0],ido,5);
        close(pipefd2[0]);
        status = msgrcv(uzenetsor,&uzi,1030,5,0);
        if(status < 0) {
          perror("Üzenetfogadási hiba\n");
          exit(EXIT_FAILURE);
        }
        char *uzenetek = strtok(uzi.text, ";");
        int i = 0;
        while(uzenetek != NULL) {
            if(i == 0) {
                strcpy(hely,uzenetek);
            }
            else if(i == 1) {
                strcpy(ido,uzenetek);
            }
            else {
                perror("Túl sok elválasztókarakter(;)\n");
                exit(EXIT_FAILURE);
            }
            i++;
            uzenetek = strtok(NULL,";");
        }
        printf("Gyerek 2 megérkezetett ide: %s, időpont: %s\n",hely, ido);
        exit(0);
    }
    else {
    sigsuspend(&sig.sa_mask);
    printf("1.kész\n");
    kill(process2,SIGINT);
    sigsuspend(&sig.sa_mask);
    printf("2.kész\n");
    printf("Adatok küldése\n");
    close(pipefd1[0]);
    write(pipefd1[1],arg1,1024);
    write(pipefd1[1],arg2,5);
    close(pipefd1[1]);
    kill(process,SIGINT);
    close(pipefd2[0]);
    write(pipefd2[1],arg1,1024);
    write(pipefd2[1],arg2,5);
    close(pipefd2[1]);
    kill(process2,SIGINT);
    status = msgrcv(uzenetsor,&uzi,1030,6,0);
    if(status < 0) {
        perror("Üzenetfogadási hiba\n");
        exit(EXIT_FAILURE);
    }
    char *uzenetek = strtok(uzi.text, ";");
    int i = 0;
    while(uzenetek != NULL) {
        if(i == 0) {
            strcpy(hely,uzenetek);
        }
        else if(i == 1) {
            strcpy(ido,uzenetek);
        }
        else {
            perror("Túl sok elválasztókarakter(;)\n");
            exit(EXIT_FAILURE);
        }
        i++;
        uzenetek = strtok(NULL,";");
    }
    int deadChildren = 0;
    while (deadChildren < 2){
        wait(NULL);
        deadChildren++;
    }
    printf("Tanár megérkezetett ide: %s, időpont: %s\n",hely, ido);
    status = msgctl(uzenetsor,IPC_RMID,NULL);
    if(status < 0) {
      perror("Hiba az üzenetsor eltávolításában\n");
      exit(EXIT_FAILURE);
    }
    }
    }
}

void main(int argc, char* argv[]) {
    key_t key = ftok(argv[0], 1);
    if(argc == 3) {
        if(strlen(argv[1]) < 1025 && strlen(argv[2]) < 5) {
            processKidsAndTeacher(key,argv[1],argv[2]);
        }
        else {
            perror("A paraméter(ek) túl hosszúak, maximum karakterlimit = 1024, idő esetében 5 (pl. 17:30)\n");
            exit(EXIT_FAILURE);
        }
    }
    else {
        perror("Nincs elég paraméter\n");
        exit(EXIT_FAILURE);
    }
}
        