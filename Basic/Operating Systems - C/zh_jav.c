#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/wait.h>
#include <fcntl.h>

struct uzenet {
    long mtype;
    char msg[1024];
};

void handler(int sig_num) {
  if(sig_num == SIGUSR1) {
    //Szignál teszt
    //printf("Szignál az 1. gyerekfolyamat felől\n");
    return;
  }
  else if (sig_num == SIGUSR2) {
    //Szignál teszt
    //printf("Szignál az 2. gyerekfolyamat felől\n");
    return;
  }
  else if (sig_num == SIGINT) {
    //Szignál teszt
    //printf("Szignál a szülő felől\n");
    return;
  }
}

void main(int argc, char* argv[]) {
    struct sigaction sig = {0};
    sig.sa_handler = &handler;
    sigemptyset(&sig.sa_mask);
    sigfillset(&sig.sa_mask);
    sigprocmask(SIG_BLOCK,&sig.sa_mask,NULL);
    sig.sa_flags = SA_RESTART;
    sigaction(SIGUSR1,&sig,NULL);
    sigaction(SIGUSR2,&sig,NULL);
    sigaction(SIGINT,&sig,NULL);

    pid_t hallgato1, hallgato2, szulo;

    key_t key = ftok(argv[0], 66);
    int uzenetsor = msgget(key, 0666 | IPC_CREAT);
    if (uzenetsor == -1)
    {
        perror("Üzenetsor létrehozási hiba");
        exit(EXIT_FAILURE);
    }

    int pipefd1[2], pipefd2[2], status;

    if (pipe(pipefd1) == -1 || pipe(pipefd2) == -1)
    {
        perror("Pipe megnyitási hiba");
        exit(EXIT_FAILURE);
    }

    hallgato1 = fork();
    if(hallgato1 < 0){
        perror("Fork hiba\n");
        exit(EXIT_FAILURE);
    }
    if (hallgato1 == 0)
    {
        szulo = getppid();
        printf("1. hallgató indul\n");
        hallgato2 = fork();
        if(hallgato2 < 0){
            perror("Fork hiba\n");
            exit(EXIT_FAILURE);
        }
        if (hallgato2 == 0)
        {
            printf("2. hallgató indul\n");

            kill(hallgato1, SIGUSR2);
            sigdelset(&sig.sa_mask,SIGUSR1);
            sigsuspend(&sig.sa_mask);

            printf("2. hallgató fogadja\n");
            char tank1_from_pipe[1024];
            close(pipefd1[1]);
            read(pipefd1[0],tank1_from_pipe,1024);
            close(pipefd1[0]);
            printf("2. hallgató küld a szülőnek\n");
            char tank2[1024];
            strcpy(tank2,"T-55");
            close(pipefd2[0]);
            write(pipefd2[1],tank1_from_pipe,1024);
            write(pipefd2[1],tank2,1024);
            close(pipefd2[1]);
            kill(szulo, SIGUSR2);
            sigsuspend(&sig.sa_mask);
            printf("2. hallgató strájk miatt taxival indul\n");
            struct uzenet uzi2;
            uzi2.mtype = 6;
            strcpy(uzi2.msg, "2.hallgató: \"Kések a visszaérkezéssel strájk miatt, taxival jövök!\"");
            status = msgsnd(uzenetsor,&uzi2,1024,0);
            if(status < 0) {
                perror("Üzenetküldési hiba\n");
                exit(EXIT_FAILURE);
            }
            kill(szulo, SIGUSR2);
            sigdelset(&sig.sa_mask,SIGINT);
            sigsuspend(&sig.sa_mask);
            struct uzenet fogaduzi;
            status = msgrcv(uzenetsor,&fogaduzi,1024,8,0);
            if(status < 0) {
                perror("Üzenetfogadási hiba\n");
                exit(EXIT_FAILURE);
            }
            printf("2. hallgató kapta : %s\n", fogaduzi.msg);
            printf("2. hallgató végez\n");
            exit(0);
        }
        else {
            sigdelset(&sig.sa_mask,SIGUSR2);
            sigsuspend(&sig.sa_mask);
            // valami
            printf("1. hallgató küldi\n");
            char tank1[1024];
            strcpy(tank1,"Tigris 1");
            close(pipefd1[0]);
            write(pipefd1[1],tank1,1024);
            close(pipefd1[1]);
            // valami
            kill(hallgato2, SIGUSR1);
            sigdelset(&sig.sa_mask,SIGINT);
            sigsuspend(&sig.sa_mask);
            kill(hallgato2, SIGUSR1);
            printf("1. hallgató strájk miatt busszal indul\n");
            struct uzenet uzi;
            uzi.mtype = 5;
            strcpy(uzi.msg, "1.hallgató: \"Kések a visszaérkezéssel strájk miatt, busszal jövök!\"");
            status = msgsnd(uzenetsor,&uzi,1024,0);
            if(status < 0) {
                perror("Üzenetküldési hiba\n");
                exit(EXIT_FAILURE);
            }
            kill(szulo, SIGUSR1);
            sigsuspend(&sig.sa_mask);
            struct uzenet fogaduzi2;
            status = msgrcv(uzenetsor,&fogaduzi2,1024,7,0);
            if(status < 0) {
                perror("Üzenetfogadási hiba\n");
                exit(EXIT_FAILURE);
            }
            printf("1. hallgató kapta : %s\n", fogaduzi2.msg);
            kill(hallgato2,SIGUSR1);
            printf("1. hallgató végez\n");
            exit(0);
        }
    }
    else if (hallgato1 > 0)
    {
        printf("Szülő indul\n");
        char uz1[1024];
        char uz2[1024];
        sigdelset(&sig.sa_mask,SIGUSR2);
        sigsuspend(&sig.sa_mask);

        close(pipefd2[1]);
        read(pipefd2[0],uz1,1024);
        read(pipefd2[0],uz2,1024);
        close(pipefd2[0]);
        printf("1. hallgató kedvenc tankja: %s\n",uz1);
        printf("2. hallgató kedvenc tankja: %s\n",uz2);
        kill(hallgato1,SIGINT);

        sigdelset(&sig.sa_mask,SIGUSR1);
        sigsuspend(&sig.sa_mask);
        sigsuspend(&sig.sa_mask);
        printf("Szülő fogadja az üzenetet\n");
        struct uzenet fogaduzi;
        struct uzenet fogaduzi2;
        status = msgrcv(uzenetsor,&fogaduzi,1024,5,0);
        if(status < 0) {
          perror("Üzenetfogadási hiba\n");
          exit(EXIT_FAILURE);
        }
        status = msgrcv(uzenetsor,&fogaduzi2,1024,6,0);
        if(status < 0) {
          perror("Üzenetfogadási hiba\n");
          exit(EXIT_FAILURE);
        }
        printf("Oktató kapta: %s\n", fogaduzi.msg);
        printf("Oktató kapta: %s\n", fogaduzi2.msg);
        printf("Szülő visszaküldi az üzenetét\n");
        struct uzenet uzi3, uzi4;
        uzi3.mtype = 7;
        uzi4.mtype = 8;
        strcpy(uzi3.msg, "Oktató: Én lefekszek, az esti vacsorát másnapra halasztjuk!");
        status = msgsnd(uzenetsor,&uzi3,1024,0);
        if(status < 0) {
            perror("Üzenetküldési hiba\n");
            exit(EXIT_FAILURE);
        }
        strcpy(uzi4.msg, "Oktató: Én lefekszek, az esti vacsorát másnapra halasztjuk!");
        status = msgsnd(uzenetsor,&uzi4,1024,0);
        if(status < 0) {
            perror("Üzenetküldési hiba\n");
            exit(EXIT_FAILURE);
        }
        kill(hallgato1, SIGINT);
        int childrenDead = 0;
        while (childrenDead != 2)
        {
            wait(NULL);
            childrenDead++;
        }
        msgctl(uzenetsor, IPC_RMID, NULL);
        printf("Szülő végez\n");
    }
}
