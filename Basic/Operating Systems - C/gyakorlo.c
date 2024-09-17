#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>

#define NAME_LENGTH 10

void out_writing(char* name, pid_t sajat, pid_t szulo) {
    printf("%s: Saját PID: %i\n", name, sajat);
    printf("%s: Szülő PID: %i\n", name, szulo);
}

void main() {
    pid_t pid = fork();
    if(pid < 0) {
        printf("Fork hiba\n");
        exit(1);
    }
    if(pid == 0){
        pid_t pid2 = fork();
        if(pid2 < 0) {
        printf("Fork hiba\n");
        exit(1);
        }
        if(pid2 == 0) {
            out_writing("Gyerek2",getpid(),getppid());
        }
        else {
            out_writing("Gyerek1",getpid(),getppid());
            wait(NULL);
        }
    }
    else{
        out_writing("Szülő",getpid(),getppid());
        wait(NULL);
    }
}