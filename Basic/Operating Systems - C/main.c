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

#define MAX_APPLICANTS 2
#define MAX_NAME_LENGTH 50
#define MAX_DAYS_LENGTH 100

#define MAX_WORKERS_MONDAY 10
#define MAX_WORKERS_TUESDAY 10
#define MAX_WORKERS_WEDNESDAY 10
#define MAX_WORKERS_THURSDAY 10
#define MAX_WORKERS_FRIDAY 10
#define MAX_WORKERS_SATURDAY 10
#define MAX_WORKERS_SUNDAY 10

#define MAX_WORKER_PER_BUS 5

#define FILE_NAME "applicants.bin"

struct Applicant
{
    char name[MAX_NAME_LENGTH];
    int available_days[7];
};

/* Napok átalakítása */
char *get_day_name(int day_index)
{
    day_index++;
    switch (day_index)
    {
    case 1:
        return "Hétfő";
    case 2:
        return "Kedd";
    case 3:
        return "Szerda";
    case 4:
        return "Csütörtök";
    case 5:
        return "Péntek";
    case 6:
        return "Szombat";
    case 7:
        return "Vasárnap";
    default:
        return "Hiba";
    }
}

char *get_day_with_suffix(int day_index)
{
    day_index++;
    switch (day_index)
    {
    case 1:
        return "Hétfőre";
    case 2:
        return "Keddre";
    case 3:
        return "Szerdára";
    case 4:
        return "Csütörtökre";
    case 5:
        return "Péntekre";
    case 6:
        return "Szombatra";
    case 7:
        return "Vasárnapra";
    default:
        return "Hiba";
    }
}
/* ----------------- */

/* Fileba írás és olvasás */
void write_num_applicants(int fd, int num_applicants)
{
    write(fd, &num_applicants, sizeof(int));
}

void write_applicant_data(int fd, struct Applicant *applicant)
{
    write(fd, applicant, sizeof(struct Applicant));
}

void write_applicant_file(struct Applicant **applicants, int num_applicants, int sendNotification)
{
    int fd = open(FILE_NAME, O_WRONLY | O_CREAT | O_TRUNC, 0644);
    if (fd == -1)
    {
        printf("Nem sikerült megnyitni a fájlt.\n");
        return;
    }

    write_num_applicants(fd, num_applicants);
    for (int i = 0; i < num_applicants; i++)
    {
        write_applicant_data(fd, applicants[i]);
    }

    close(fd);
    if(sendNotification == 1) {
        printf("A program adatai mentésre kerültek a \"%s\" fileba.\n", FILE_NAME);
    }
}

void read_applicant_file(struct Applicant ***applicants, int *num_applicants, int *capacity)
{
    int fd = open(FILE_NAME, O_RDONLY | O_CREAT, 0644);
    if (fd == -1)
    {
        return;
    }

    read(fd, num_applicants, sizeof(int));
    *capacity = *num_applicants;

    if (*capacity == 0)
    {
        *num_applicants = 2;
    }

    *applicants = malloc(*num_applicants * sizeof(struct Applicant *));

    if (*capacity == 0)
    {
        *capacity = 2;
        *num_applicants = 0;
    }

    if (*applicants == NULL)
    {
        printf("Nem sikerült beolvasni a file tartalmát.\n");
        exit(1);
    }
    for (int i = 0; i < *num_applicants; i++)
    {
        struct Applicant *new_applicant = malloc(sizeof(struct Applicant));
        if (new_applicant == NULL)
        {
            printf("Nem sikerült a memória lefoglalása.\n");
            exit(1);
        }
        read(fd, new_applicant, sizeof(struct Applicant));
        (*applicants)[i] = new_applicant;
    }
    close(fd);
    printf("A kezdeti adatok beolvasásra kerültek a \"%s\" fileból.\n", FILE_NAME);
}
/* ------------------------- */

/* Memória felszabadítás */
void free_applicants(struct Applicant ***applicants, int num_applicants)
{
    for (int i = 0; i < num_applicants; i++)
    {
        free((*applicants)[i]);
    }
    free(*applicants);
}
/* --------------------- */

/* Jelentkezők hozzáadása */
void read_applicant_data(struct Applicant *applicant)
{
    printf("Add meg az új jelentkező nevét:\n> ");
    fgets(applicant->name, MAX_NAME_LENGTH, stdin);
    applicant->name[strcspn(applicant->name, "\n")] = '\0';
}

void add_applicant(struct Applicant ***applicants, int *num_applicants, int *capacity)
{
    if (*num_applicants == *capacity)
    {
        *capacity *= 2;
        struct Applicant **temp = realloc(*applicants, *capacity * sizeof(struct Applicant *));
        if (temp == NULL)
        {
        }
        *applicants = temp;
    }

    struct Applicant *new_applicant = malloc(sizeof(struct Applicant));
    if (new_applicant == NULL)
    {
        printf("Nem sikerült lefoglalni a memóriaterületet.\n");
        exit(1);
    }

    for (int j = 0; j < 7; j++)
    {
        new_applicant->available_days[j] = 0;
    }

    read_applicant_data(new_applicant);

    int already_exists = 0;
    for (int i = 0; i < *num_applicants; i++)
    {
        if (strcmp((*applicants)[i]->name, new_applicant->name) == 0)
        {
            already_exists = 1;
        }
    }

    if (already_exists == 0)
    {
        printf("Add meg a napokat szóközzel elválasztva\nPéldául: Hétfő Kedd Szerda\n> ");
        char available_days_str[MAX_DAYS_LENGTH];
        fgets(available_days_str, MAX_DAYS_LENGTH, stdin);
        available_days_str[strcspn(available_days_str, "\n")] = '\0';

        int working_days = 0;

        char *day_token = strtok(available_days_str, " ");
        while (day_token != NULL)
        {
            int day_index;
            int max_workers_this_day = 0;
            if (strcmp(day_token, "Hétfő") == 0)
            {
                day_index = 0;
                max_workers_this_day = MAX_WORKERS_MONDAY;
            }
            else if (strcmp(day_token, "Kedd") == 0)
            {
                day_index = 1;
                max_workers_this_day = MAX_WORKERS_TUESDAY;
            }
            else if (strcmp(day_token, "Szerda") == 0)
            {
                day_index = 2;
                max_workers_this_day = MAX_WORKERS_WEDNESDAY;
            }
            else if (strcmp(day_token, "Csütörtök") == 0)
            {
                day_index = 3;
                max_workers_this_day = MAX_WORKERS_THURSDAY;
            }
            else if (strcmp(day_token, "Péntek") == 0)
            {
                day_index = 4;
                max_workers_this_day = MAX_WORKERS_FRIDAY;
            }
            else if (strcmp(day_token, "Szombat") == 0)
            {
                day_index = 5;
                max_workers_this_day = MAX_WORKERS_SATURDAY;
            }
            else if (strcmp(day_token, "Vasárnap") == 0)
            {
                day_index = 6;
                max_workers_this_day = MAX_WORKERS_SUNDAY;
            }
            else
            {
                printf("Hibás napot adtál meg (%s)\n", day_token);
                day_token = strtok(NULL, " ");
                continue;
            }

            if (new_applicant->available_days[day_index])
            {
                printf("\"%s\" már jelentkezett %s.\n", new_applicant->name, get_day_with_suffix(day_index));
            }
            else
            {
                int num_workers_for_day = 0;
                for (int i = 0; i < *num_applicants; i++)
                {
                    if ((*applicants)[i]->available_days[day_index])
                    {
                        num_workers_for_day++;
                    }
                }
                if (num_workers_for_day >= max_workers_this_day)
                {
                    printf("%s már megtelt a munkalehetőségek száma. (%d fő)\n", get_day_with_suffix(day_index), max_workers_this_day);
                }
                else
                {
                    new_applicant->available_days[day_index] = 1;
                    printf("\"%s\" jelentkező hozzáadásra került %s.\n", new_applicant->name, get_day_with_suffix(day_index));
                    working_days++;
                }
            }

            day_token = strtok(NULL, " ");
        }

        if (working_days > 0)
        {
            (*applicants)[*num_applicants] = new_applicant;
            (*num_applicants)++;
        }
        else
        {
            printf("\"%s\" jelentkező nem került hozzáadásra, mivel egyetlen nap sem dolgozik.\n", new_applicant->name);
            free(new_applicant);
        }
    }
    else
    {
        printf("A dolgozó a neve alapján már létezik a rendszerben, így nem került hozzáadásra. (%s)\n", new_applicant->name);
    }
}
/* ---------------------- */

/* Törlés */
void delete_applicant(struct Applicant ***applicants, int *num_applicants, char *name)
{
    int index = -1;
    for (int i = 0; i < *num_applicants; i++)
    {
        if (strcmp((*applicants)[i]->name, name) == 0)
        {
            index = i;
            break;
        }
    }

    if (index == -1)
    {
        printf("A megadott jelentkező neve nem található a listában.\n");
    }
    else
    {
        for (int i = index; i < *num_applicants - 1; i++)
        {
            (*applicants)[i] = (*applicants)[i + 1];
        }

        (*num_applicants)--;
        printf("\"%s\" jelentkező sikeresen törlésre került a listából.\n", name);
    }
}
/* ------ */

/* Napok módosítása */
void modify_applicant_days(struct Applicant ***applicants, int num_applicants, char *name)
{
    int index = -1;
    for (int i = 0; i < num_applicants; i++)
    {
        if (strcmp((*applicants)[i]->name, name) == 0)
        {
            index = i;
            break;
        }
    }

    if (index == -1)
    {
        printf("A megadott jelentkező neve nem található a listában.\n");
    }
    else
    {
        struct Applicant *temp_applicant = malloc(sizeof(struct Applicant));
        if (temp_applicant == NULL)
        {
            printf("Nem sikerült lefoglalni a memóriaterületet.\n");
            exit(1);
        }

        for (int j = 0; j < 7; j++)
        {
            temp_applicant->available_days[j] = (*applicants)[index]->available_days[j];
            (*applicants)[index]->available_days[j] = 0;
        }

        printf("Add meg a napokat szóközzel elválasztva\nPéldául: Hétfő Kedd Szerda\n> ");
        char available_days_str[MAX_DAYS_LENGTH];
        fgets(available_days_str, MAX_DAYS_LENGTH, stdin);
        available_days_str[strcspn(available_days_str, "\n")] = '\0';

        int working_days = 0;

        char *day_token = strtok(available_days_str, " ");
        while (day_token != NULL)
        {
            int day_index;
            int max_workers_this_day = 0;
            if (strcmp(day_token, "Hétfő") == 0)
            {
                day_index = 0;
                max_workers_this_day = MAX_WORKERS_MONDAY;
            }
            else if (strcmp(day_token, "Kedd") == 0)
            {
                day_index = 1;
                max_workers_this_day = MAX_WORKERS_TUESDAY;
            }
            else if (strcmp(day_token, "Szerda") == 0)
            {
                day_index = 2;
                max_workers_this_day = MAX_WORKERS_WEDNESDAY;
            }
            else if (strcmp(day_token, "Csütörtök") == 0)
            {
                day_index = 3;
                max_workers_this_day = MAX_WORKERS_THURSDAY;
            }
            else if (strcmp(day_token, "Péntek") == 0)
            {
                day_index = 4;
                max_workers_this_day = MAX_WORKERS_FRIDAY;
            }
            else if (strcmp(day_token, "Szombat") == 0)
            {
                day_index = 5;
                max_workers_this_day = MAX_WORKERS_SATURDAY;
            }
            else if (strcmp(day_token, "Vasárnap") == 0)
            {
                day_index = 6;
                max_workers_this_day = MAX_WORKERS_SUNDAY;
            }
            else
            {
                printf("Hibás napot adtál meg (%s)\n", day_token);
                day_token = strtok(NULL, " ");
                continue;
            }

            if ((*applicants)[index]->available_days[day_index])
            {
                printf("\"%s\" már jelentkezett %s.\n", (*applicants)[index]->name, get_day_with_suffix(day_index));
            }
            else
            {
                int num_workers_for_day = 0;
                for (int i = 0; i < num_applicants; i++)
                {
                    if ((*applicants)[i]->available_days[day_index])
                    {
                        num_workers_for_day++;
                    }
                }
                if (num_workers_for_day >= max_workers_this_day)
                {
                    printf("%s már megtelt a munkalehetőségek száma. (%d fő)\n", get_day_with_suffix(day_index), max_workers_this_day);
                }
                else
                {
                    (*applicants)[index]->available_days[day_index] = 1;
                    printf("\"%s\" jelentkező hozzáadásra került %s.\n", (*applicants)[index]->name, get_day_with_suffix(day_index));
                    working_days++;
                }
            }

            day_token = strtok(NULL, " ");
        }

        if (working_days > 0)
        {
            printf("Sikeresen módosítottad \"%s\" jelentkező munkanapjait.\n", (*applicants)[index]->name);
        }
        else
        {
            printf("\"%s\" jelentkező nem került frissítésre, mivel a frissítés után egyetlen nap sem dolgozna.\n", (*applicants)[index]->name);
            for (int j = 0; j < 7; j++)
            {
                (*applicants)[index]->available_days[j] = temp_applicant->available_days[j];
            }
        }
        free(temp_applicant);
    }
}
/* ---------------- */

/* Kiírások */
void print_applicants(struct Applicant **applicants, int num_applicants)
{
    printf("Az összes jelentkező:\n");
    if (num_applicants == 0)
    {
        printf("Jelenleg egy jelentkező sincs az adatbázisban.\n");
    }
    else
    {
        for (int i = 0; i < num_applicants; i++)
        {
            printf("\"%s\" - Elérhető napok: ", applicants[i]->name);
            for (int j = 0; j < 7; j++)
            {
                if (applicants[i]->available_days[j] != 0)
                {
                    printf("%s ", get_day_name(j));
                }
            }
            printf("\n");
        }
    }
}

void print_applicants_for_day(struct Applicant **applicants, int num_applicants, int day_index)
{
    day_index--;
    printf("%s jelentkezők:\n", get_day_with_suffix(day_index));
    for (int i = 0; i < num_applicants; i++)
    {
        if (applicants[i]->available_days[day_index])
        {
            printf("- %s\n", applicants[i]->name);
        }
    }
}
/* -------- */

/* 2. beadandó függvényei */
struct msg_t {
    long mtype;
    char namesOnBus[2*MAX_WORKER_PER_BUS][MAX_NAME_LENGTH];
};

void handler(int signum) {
  if(signum == SIGUSR1) {
    //printf("Signal: gyerekfolyamat->szülő\n");
  }
  else if (signum == SIGUSR2) {
    //printf("Signal: szülő->gyerekfolyamat\n");
  }
}

void launchBuses(struct Applicant **applicants, int num_applicants, key_t key, int today) {
    int pipefd[2];
    if(pipe(pipefd) == -1){
        printf("Hiba történt a cső megnyitásakor.\n");
        exit(1);
    }

    struct sigaction sigact;
    sigact.sa_handler = &handler;
    sigact.sa_flags = 0;
    sigemptyset(&sigact.sa_mask);
    sigfillset(&sigact.sa_mask);
    sigdelset(&sigact.sa_mask, SIGUSR1);
    sigdelset(&sigact.sa_mask, SIGUSR2);
    sigprocmask(SIG_BLOCK, &sigact.sa_mask, NULL);
    sigaction(SIGUSR1, &sigact, NULL);
    sigaction(SIGUSR2, &sigact, NULL);

    int mq_id = msgget(key, 0600 | IPC_CREAT);
    if(mq_id < 0) {
        printf("Hiba történt az üzenetsor megnyitásakor.\n");
        exit(1);
    }

    pid_t bus1;
    bus1 = fork();
    
    if(bus1 < 0) {
        printf("Hiba történt a gyerekfolyamat megnyitásakor.\n");
        exit(1);
    }

    if(bus1 == 0) { // gyerek - 1. busz
        char bus1Seats[MAX_WORKER_PER_BUS][MAX_NAME_LENGTH];
        char bus2Seats[MAX_WORKER_PER_BUS][MAX_NAME_LENGTH];
        int worker_count = 0;

        kill(getppid(), SIGUSR1);
        sigsuspend(&sigact.sa_mask);

        struct msg_t msg_to_parent;
        msg_to_parent.mtype = 5;

        int temp;
        close(pipefd[1]);
        printf("Busz 1-en ülők:\n");
        while (worker_count < 5 && (temp = read(pipefd[0], bus1Seats[worker_count], MAX_NAME_LENGTH*sizeof(char))) > 0) {
            printf("- %s\n", bus1Seats[worker_count]);
            strcpy(msg_to_parent.namesOnBus[worker_count], bus1Seats[worker_count]);
            worker_count++;
        }

        if(worker_count == 0) {
            printf("Senki sem ül az 1-es buszon.\n");
        }
        else {
            temp = 0;
            printf("Busz 2-n ülők:\n");
            while (worker_count >= 5 && (temp = read(pipefd[0], bus2Seats[worker_count%5], MAX_NAME_LENGTH*sizeof(char))) > 0) {
                printf("- %s\n", bus2Seats[worker_count%5]);
                strcpy(msg_to_parent.namesOnBus[worker_count], bus2Seats[worker_count%5]);
                worker_count++;
            }

            if(worker_count < 5) {
                printf("Senki sem ül a 2-es buszon.\n");
            }
        }
    
        if(worker_count == 0) {
            printf("A mai nap nem indult buszjárat, mivel senki sem dolgozik ezen a napon.\n");
        }
        else if(worker_count < 5) {
            printf("Az 1-es busz elindult a dolgozókkal.\n");

            if(msgsnd(mq_id, &msg_to_parent, 2*MAX_WORKER_PER_BUS*MAX_NAME_LENGTH*sizeof(char), 0) < 0) {
                printf("Hiba az üzenet küldése közben\n");
                exit(1);
            }
        }
        else if(worker_count >= 5) {
            printf("Az 1-es és a 2-es busz elindult a dolgozókkal.\n");

            if(msgsnd(mq_id, &msg_to_parent, 2*MAX_WORKER_PER_BUS*MAX_NAME_LENGTH*sizeof(char), 0) < 0) {
                printf("Hiba az üzenet küldése közben\n");
                exit(1);
            }
        }

        close(pipefd[0]);
        sigprocmask(SIG_UNBLOCK, &sigact.sa_mask, NULL);

        exit(0);
    }
    else { // szülő
        sigsuspend(&sigact.sa_mask);

        close(pipefd[0]);
        int workers_this_day = 0;
        for (int i = 0; i < num_applicants; i++)
        {
            if (applicants[i]->available_days[today])
            {
                write(pipefd[1], applicants[i]->name, MAX_NAME_LENGTH*sizeof(char));
                //printf("Szülő ír - %s\n", applicants[i]->name);
                workers_this_day++;
            }
        }
        close(pipefd[1]);
        kill(bus1, SIGUSR2);
        printf("Szőlészet elküldte a dolgozók neveit a buszoknak.\n");

        wait(NULL);
        sleep(2);

        // kikérjük az üzenetsorból az elszállítottakat
        if (workers_this_day != 0) {
            struct msg_t msg_from_child;
            if (msgrcv(mq_id, &msg_from_child, 2*MAX_WORKER_PER_BUS*MAX_NAME_LENGTH*sizeof(char), 5, 0) != -1) {
                printf("A busz leszállította a dolgozókat!\n");
                printf("1. buszon ülők:\n");
                int i = 0;
                while(i < workers_this_day) {
                    if(i == 5) {
                        printf("2. buszon ülők:\n");
                    }
                    printf("- %s\n", msg_from_child.namesOnBus[i]);
                    i++;
                }
            }
        }

        msgctl(mq_id, IPC_RMID, NULL);
        fflush(NULL);
    }
}
/* ---------------------- */

int main(int argc, char* argv[])
{
    struct Applicant **applicants = NULL;
    int capacity = MAX_APPLICANTS;
    int num_applicants = 0;

    key_t key = ftok(argv[0], 66);
    int today = 0;

    read_applicant_file(&applicants, &num_applicants, &capacity);

    while (1)
    {
        printf("\nJelenleg '%s' van!\nA mai járatok már elindultak, a holnapiak indításához válaszd a 6-os menüpontot.", get_day_name(today));
        printf("\nMenü:\n");
        printf("1. Jelentkező hozzáadása\n");
        printf("2. Jelentkező törlése\n");
        printf("3. Jelentkezők listája egy adott napon\n");
        printf("4. Az összes jelentkező\n");
        printf("5. Hozzáadott jelentkező napjainak frissítése\n");
        printf("6. Következő napi járatok indítása\n");
        printf("7. Kilépés\n");
        printf("Válassz lehetőséget: ");

        int choice;
        scanf("%d", &choice);
        getchar();

        switch (choice)
        {
        case 1:
            add_applicant(&applicants, &num_applicants, &capacity);
            write_applicant_file(applicants, num_applicants, 0);
            break;
        case 2:
            printf("Add meg a jelentkező nevét a törléshez:\n> ");
            char name[MAX_NAME_LENGTH];
            fgets(name, MAX_NAME_LENGTH, stdin);
            name[strcspn(name, "\n")] = '\0';
            delete_applicant(&applicants, &num_applicants, name);
            write_applicant_file(applicants, num_applicants, 0);
            break;
        case 3:
            printf("Add meg a nap sorszámát (1-7, 1 a hétfő): ");
            int day_index;
            scanf("%d", &day_index);
            getchar();
            print_applicants_for_day(applicants, num_applicants, day_index);
            break;
        case 4:
            print_applicants(applicants, num_applicants);
            break;
        case 5:
            printf("Add meg a jelentkező nevét a napok frissítéséhez:\n> ");
            char modify_name[MAX_NAME_LENGTH];
            fgets(modify_name, MAX_NAME_LENGTH, stdin);
            modify_name[strcspn(modify_name, "\n")] = '\0';
            modify_applicant_days(&applicants, num_applicants, modify_name);
            write_applicant_file(applicants, num_applicants, 0);
            break;
        case 6:
            today++;
            if(today > 6) {
                today = 0;
            }
            launchBuses(applicants, num_applicants, key, today);
            break;
        case 7:
            write_applicant_file(applicants, num_applicants, 1);
            free_applicants(&applicants, num_applicants);
            return 0;
        default:
            printf("Helytelen lehetőség.\n");
        }
    }

    return 0;
}