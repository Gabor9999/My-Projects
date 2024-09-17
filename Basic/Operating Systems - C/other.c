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

void main() {
    pid_t pid1, pid2;
    int pipe1[2], pipe2[2];

    if (pipe(pipe1) == -1 || pipe(pipe2) == -1) {
        perror("pipe");
        exit(EXIT_FAILURE);
    }

    pid1 = fork();
    if (pid1 == 0)
    {
        sigdelset(&sigset,SIGUSR2);
        sigsuspend(&sigset);

        close(pipe1[1]);
        close(pipe2[0]);
        close(pipe2[1]);

        kill(getppid(), SIGUSR1);
        sigsuspend(&sigset);

        char read_buf[1024];

        read(pipe1[0], read_buf, sizeof(read_buf));
        printf("%i utaslista:\n%s",getpid(), read_buf);
        close(pipe1[0]);
        kill(getppid(),SIGUSR2);

        int personnel = 0;
        for (int i = 0; i < strlen(read_buf); i++) {
            if (read_buf[i] == '\n') {
                personnel++;
            }
        }
        memset(read_buf, 0, sizeof(read_buf));

        sprintf(msg.mtext, "%i szamu busz %i / %i fot hozott be\n", getpid(), personnel, personnel);
        msgsnd(msqid, &msg, sizeof(msg.mtext), 0);

        exit(0);
        }
    else if (pid1 > 0) {
        pid2 = fork();
        if (pid2 == 0) {
            sigdelset(&sigset,SIGUSR2);
            sigsuspend(&sigset);
            close(pipe1[0]);
            close(pipe1[1]);
            close(pipe2[1]);

            kill(getppid(), SIGUSR1);
            sigsuspend(&sigset);

            char read_buf[1024];

            read(pipe2[0], read_buf, sizeof(read_buf));
            printf("%i utaslista:\n%s",getpid(), read_buf);
            close(pipe2[0]);

            int personnel = 0;
            for (int i = 0; i < strlen(read_buf); i++) {
                if (read_buf[i] == '\n') {
                    personnel++;
                }
            }
            memset(read_buf, 0, sizeof(read_buf));

            sprintf(msg.mtext, "%i szamu busz %i / %i fot hozott be\n", getpid(), personnel, personnel);
            msgsnd(msqid, &msg, sizeof(msg.mtext), 0);
            kill(getppid(),SIGUSR2);

            exit(0);
        }

        else if (pid2 > 0){
            printf("Keszitem a %s napi jaratokat\n", numToDay(dayIndex));
            sigdelset(&sigset, SIGUSR1);
            kill(pid1, SIGUSR2);
            sigsuspend(&sigset);
            kill(pid2, SIGUSR2);
            sigsuspend(&sigset);

            close(pipe1[0]);
            close(pipe2[0]);

            int indexOfWorkers = 0;
            char concat_str1[1024] = "";
            char concat_str2[1024] = "";
            for (int i = 0; i < countOfWorkers; ++i) {
                if (strstr(workers[i].days, numToDay(dayIndex)) != NULL)
                    {
                        if (indexOfWorkers < 5)
                        {
                            strcat(concat_str1, workers[i].name);
                            strcat(concat_str1, "\n");
                        }
                        else
                        {
                            strcat(concat_str2, workers[i].name);
                            strcat(concat_str2, "\n");
                        }
                        indexOfWorkers++;
                    }
                }
                write(pipe1[1], concat_str1, strlen(concat_str1) + 1);
                close(pipe1[1]);
                write(pipe2[1], concat_str2, strlen(concat_str2) + 1);
                close(pipe2[1]);

                sigdelset(&sigset, SIGUSR2);
                kill(pid1,SIGUSR2);
                sigsuspend(&sigset);
                kill(pid2,SIGUSR2);
                sigsuspend(&sigset);

                msgrcv(msqid, &msg, sizeof(msg.mtext), 1, 0);
                printf("\n%s", msg.mtext);
                msgrcv(msqid, &msg, sizeof(msg.mtext), 1, 0);
                printf("%s\n", msg.mtext);
                int deadChildrenCount = 0;
                while (deadChildrenCount < 2)
                {
                    wait(NULL);
                    deadChildrenCount++;
                }
                dayIndex++;
            }
        }
    }
        