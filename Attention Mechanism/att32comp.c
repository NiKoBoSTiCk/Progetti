#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <string.h>
#include <time.h>
#include <xmmintrin.h>
#include <omp.h>

#define	type		float
#define	MATRIX		type*
#define	VECTOR		type*
#define RISULTATO   "test_2048_48_32.os"

typedef struct {
    MATRIX ds; 	// dataset
    MATRIX wq; 	// pesi WQ
    MATRIX wk; 	// pesi WK
    MATRIX wv; 	// pesi WV
    MATRIX out;	// matrice contenente risultato (N x nn)
    VECTOR bq; 	// pesi bq
    VECTOR bk; 	// pesi bk
    VECTOR bv; 	// pesi bv
    int N;		// numero di righe del dataset
    int s; 		// prima dimensione del tensore S
    int n; 		// seconda dimensione del tensore S
    int d; 		// terza dimensione del tensore S
    int ns; 	// numero di tensori nel dataset
    int nn;		// numero di neuroni
    int display;
    int silent;
} params;

void* get_block(int size, int elements) {
    return _mm_malloc(elements*size,16);
}

void free_block(void* p) {
    _mm_free(p);
}

MATRIX alloc_matrix(int rows, int cols) {
    return (MATRIX) get_block(sizeof(type),rows*cols);
}

void dealloc_matrix(MATRIX mat) {
    free_block(mat);
}

/*
*
* 	load_data
* 	=========
*
*	Legge da file una matrice di N righe
* 	e M colonne e la memorizza in un array lineare in row-major order
*
* 	Codifica del file:
* 	primi 4 byte: numero di righe (N) --> numero intero
* 	successivi 4 byte: numero di colonne (M) --> numero intero
* 	successivi N*M*4 byte: matrix data in row-major order --> numeri floating-point a precisione singola
*
*/
MATRIX load_data(char* filename, int *n, int *k) {
    FILE* fp;
    int rows, cols, status, i;

    fp = fopen(filename, "rb");

    if (fp == NULL){
        printf("'%s': bad data file name!\n", filename);
        exit(0);
    }

    status = fread(&cols, sizeof(int), 1, fp);
    status = fread(&rows, sizeof(int), 1, fp);

    MATRIX data = alloc_matrix(rows,cols);
    status = fread(data, sizeof(type), rows*cols, fp);
    fclose(fp);

    *n = rows;
    *k = cols;

    return data;
}

/*
* 	save_data
* 	=========
*
*	Salva su file un array lineare in row-major order
*	come matrice di N righe e M colonne
*
* 	Codifica del file:
* 	primi 4 byte: numero di righe (N) --> numero intero a 32 bit
* 	successivi 4 byte: numero di colonne (M) --> numero intero a 32 bit
* 	successivi N*M*4 byte: matrix data in row-major order --> numeri interi o floating-point a precisione singola
*/
void save_data(char* filename, void* X, int n, int k) {
    FILE* fp;
    int i;
    fp = fopen(filename, "wb");
    if(X != NULL){
        fwrite(&k, 4, 1, fp);
        fwrite(&n, 4, 1, fp);
        for (i = 0; i < n; i++) {
            fwrite(X, sizeof(type), k, fp);
            //printf("%i %i\n", ((int*)X)[0], ((int*)X)[1]);
            X += sizeof(type)*k;
        }
    }
    else{
        int x = 0;
        fwrite(&x, 4, 1, fp);
        fwrite(&x, 4, 1, fp);
    }
    fclose(fp);
}

// PROCEDURE ASSEMBLY
extern void get_matrix_ott(MATRIX M, MATRIX out, int start, int end);
extern void set_matrix_ott(MATRIX M, MATRIX out, int start, int end);
extern MATRIX divisione_tensore_ott(MATRIX S, int dimS, float d);
extern MATRIX f_ott(MATRIX S, int dimS);
extern MATRIX prodotto_matrici_ott(MATRIX A, int ra, int ca, MATRIX B, int cb);

MATRIX prodotto_matrici(MATRIX A, int ra, int ca, MATRIX B, int cb) {
    float somma;
    MATRIX ret = alloc_matrix(ra, cb);

    for (int i = 0; i < ra; i++)
        for (int j = 0; j < cb; j++) {
            somma = 0;
            for (int k = 0; k < ca; k++)
                somma += A[i*ca + k] * B[k*cb + j]; //ret[i][j]=A[i][k]*B[k][j]
            ret[i*cb + j] = somma;
        }
    return ret;
}

MATRIX combinazione_lineare(MATRIX S, MATRIX W, VECTOR b, params* input) {
    int s  = input->s;
    int n  = input->n;
    int d  = input->d;
    int nn = input->nn;
    MATRIX ret = alloc_matrix(s, n * nn);
    MATRIX Ai  = alloc_matrix(n, d);

    for (int i = 0; i < s; i++) { //per ogni matrice nel tensore
        get_matrix_ott(S, Ai, n*d*i, n*d*(i + 1)); //la copio in Ai
        //la moltiplico per W (d x nn) ottenendo Ai x W (n x nn)
        MATRIX AiW = prodotto_matrici_ott(Ai, n, d, W, nn);
        for (int j = n*nn*i, c = 0; j < n*nn*(i+1); j++, c++)
            ret[j] = AiW[c] + b[c % nn]; //la copio nel risultato e sommo il bias
    }
    return ret;
}

MATRIX prodotto_tensori(MATRIX Sa, int ra, int ca, MATRIX Sb, int cb, params* input) {
    int s = input->s;
    MATRIX Sab = alloc_matrix(s, ra*cb);
    MATRIX Ai  = alloc_matrix(ra, ca);
    MATRIX Bi  = alloc_matrix(ca, cb);

    for (int i = 0; i < s; i++) { //per ogni matrice nei tensori
        //copio l'i-esima matrice del primo tensore in Ai
        get_matrix_ott(Sa, Ai, ra*ca*i, ra*ca*(i + 1));
        //copio l'i-esima matrice del secondo tensore in Bi
        get_matrix_ott(Sb, Bi, ca*cb*i, ca*cb*(i + 1));
        //ne calcolo il prodotto Ai x Bi (ra x cb)
        MATRIX AB = prodotto_matrici_ott(Ai, ra, ca, Bi, cb);
        //lo copio del risultato
        set_matrix_ott(AB, Sab, ra*cb*i, ra*cb*(i+1));
    }
    return Sab;
}

MATRIX trasposta_tensore(MATRIX S, params* input) {
    int s  = input->s;
    int n  = input->n;
    int nn = input->nn;
    MATRIX St = alloc_matrix(s, n*nn);

    for(int k = 0; k < s; k++)  //per ogni matrice nel tensore
        for(int i = 0; i < n; i++)
            for(int j = 0; j < nn; j++)
                St[(j*n + i + k*n*nn)] = S[(i*nn + j + k*n*nn)]; //T[j][i] = A[i][j]
    return St;
}

MATRIX divisione_tensore(MATRIX S, float d, params* input) {
    int s = input->s;
    int n = input->n;

    for (int i = 0; i < s*n*n; i++)  //per ogni elemento nel tensore
        S[i] /= d; //lo divido per d
    return S;
}

MATRIX f(MATRIX S, params* input) {
    int s = input->s;
    int n = input->n;

    for (int i = 0; i < s*n*n; i++) //per ogni elemento nel tensore
        S[i] = (((S[i] >= 0)? 1: -1) * ((-1.0/(S[i] + 2.0)) + 0.5)) + 0.5; //calcolo f(S[i])
    return S;
}

void att(params* input){
    int s  = input->s;
    int n  = input->n;
    int nn = input->nn;
    int d  = input->d;
    int ns = input->ns;
    MATRIX S = alloc_matrix(s, n * d);

#pragma omp parallel num_threads(ns)
    { //per ogni tensore nel dataset
        #pragma omp critical
        {
            int i = omp_get_thread_num();
            //copio il tensore in T
            get_matrix_ott(input->ds, S, s*n*d*i, s*n*d*(i + 1));

            /* prima fase */
            MATRIX Q = combinazione_lineare(S, input->wq, input->bq, input);
            MATRIX K = combinazione_lineare(S, input->wk, input->bk, input);
            MATRIX V = combinazione_lineare(S, input->wv, input->bv, input);

            /* seconda fase */
            MATRIX Kt  = trasposta_tensore(K, input);
            MATRIX QKt = prodotto_tensori(Q, n, nn, Kt, n, input);
            MATRIX S1  = divisione_tensore_ott(QKt, s*n*n, sqrt(d));

            /* terza fase */
            MATRIX S2 = f_ott(S1, s*n*n);

            /* quarta fase */
            MATRIX Sf = prodotto_tensori(S2, n, n, V, nn, input);

            //copio il tensore finale nel dataset out
            set_matrix_ott(Sf, input->out, s*n*nn*i, s*n*nn*(i+1));
        }
    }
}

int main(int argc, char** argv) {
    char fname[256];
    char* dsfilename = NULL;
    char* wqfilename = NULL;
    char* wkfilename = NULL;
    char* wvfilename = NULL;
    char* bqfilename = NULL;
    char* bkfilename = NULL;
    char* bvfilename = NULL;
    clock_t t;
    float time;

    //
    // Imposta i valori di default dei parametri
    //

    params* input = malloc(sizeof(params));

    input->ds = NULL;
    input->wq = NULL;
    input->wk = NULL;
    input->wv = NULL;
    input->bq = NULL;
    input->bk = NULL;
    input->bv = NULL;
    input->s = -1;
    input->n = -1;
    input->d = -1;
    input->ns = -1;

    input->silent = 0;
    input->display = 0;

    //
    // Visualizza la sintassi del passaggio dei parametri da riga comandi
    //

    if(argc <= 1){
        printf("%s -ds <DS> -wq <WQ> -wk <WK> -wv <WV> -bq <bQ> -bk <bK> -bv <bV> -si <si> -n <n> [-s] [-d]\n", argv[0]);
        printf("\nParameters:\n");
        printf("\tDS: il nome del file ds2 contenente il dataset\n");
        printf("\tWQ: il nome del file ds2 contenente i pesi WQ\n");
        printf("\tWK: il nome del file ds2 contenente i pesi WK\n");
        printf("\tWV: il nome del file ds2 contenente i pesi WV\n");
        printf("\tbQ: il nome del file ds2 contenente i pesi bQ\n");
        printf("\tbK: il nome del file ds2 contenente i pesi bK\n");
        printf("\tbV: il nome del file ds2 contenente i pesi bV\n");
        printf("\tN: numero di righe del dataset\n");
        printf("\tsi: prima dimensione del tensore\n");
        printf("\tn: seconda dimensione del tensore\n");
        printf("\tnn: numero di neuroni\n");
        printf("\nOptions:\n");
        printf("\t-s: modo silenzioso, nessuna stampa, default 0 - false\n");
        printf("\t-d: stampa a video i risultati, default 0 - false\n");
        exit(0);
    }

    //
    // Legge i valori dei parametri da riga comandi
    //

    int par = 1;
    while (par < argc) {
        if (strcmp(argv[par],"-s") == 0) {
            input->silent = 1;
            par++;
        } else if (strcmp(argv[par],"-d") == 0) {
            input->display = 1;
            par++;
        } else if (strcmp(argv[par],"-ds") == 0) {
            par++;
            if (par >= argc) {
                printf("Missing dataset file name!\n");
                exit(1);
            }
            dsfilename = argv[par];
            par++;
        } else if (strcmp(argv[par],"-wq") == 0) {
            par++;
            if (par >= argc) {
                printf("Missing wq file name!\n");
                exit(1);
            }
            wqfilename = argv[par];
            par++;
        } else if (strcmp(argv[par],"-wk") == 0) {
            par++;
            if (par >= argc) {
                printf("Missing wk file name!\n");
                exit(1);
            }
            wkfilename = argv[par];
            par++;
        } else if (strcmp(argv[par],"-wv") == 0) {
            par++;
            if (par >= argc) {
                printf("Missing wv file name!\n");
                exit(1);
            }
            wvfilename = argv[par];
            par++;
        } else if (strcmp(argv[par],"-bq") == 0) {
            par++;
            if (par >= argc) {
                printf("Missing bq file name!\n");
                exit(1);
            }
            bqfilename = argv[par];
            par++;
        } else if (strcmp(argv[par],"-bk") == 0) {
            par++;
            if (par >= argc) {
                printf("Missing bk file name!\n");
                exit(1);
            }
            bkfilename = argv[par];
            par++;
        } else if (strcmp(argv[par],"-bv") == 0) {
            par++;
            if (par >= argc) {
                printf("Missing bv file name!\n");
                exit(1);
            }
            bvfilename = argv[par];
            par++;
        } else if (strcmp(argv[par],"-si") == 0) {
            par++;
            if (par >= argc) {
                printf("Missing si value!\n");
                exit(1);
            }
            input->s = atoi(argv[par]);
            par++;
        } else if (strcmp(argv[par],"-n") == 0) {
            par++;
            if (par >= argc) {
                printf("Missing n value!\n");
                exit(1);
            }
            input->n = atoi(argv[par]);
            par++;
        } else{
            printf("WARNING: unrecognized parameter '%s'!\n",argv[par]);
            par++;
        }
    }

    //
    // Legge i dati e verifica la correttezza dei parametri
    //

    if(dsfilename == NULL || strlen(dsfilename) == 0){
        printf("Missing ds file name!\n");
        exit(1);
    }

    if(wqfilename == NULL || strlen(wqfilename) == 0){
        printf("Missing wq file name!\n");
        exit(1);
    }

    if(wkfilename == NULL || strlen(wkfilename) == 0){
        printf("Missing wk file name!\n");
        exit(1);
    }

    if(wvfilename == NULL || strlen(wvfilename) == 0){
        printf("Missing wv file name!\n");
        exit(1);
    }

    if(bqfilename == NULL || strlen(bqfilename) == 0){
        printf("Missing bq file name!\n");
        exit(1);
    }

    if(bkfilename == NULL || strlen(bkfilename) == 0){
        printf("Missing bk file name!\n");
        exit(1);
    }

    if(bvfilename == NULL || strlen(bvfilename) == 0){
        printf("Missing bv file name!\n");
        exit(1);
    }

    input->ds = load_data(dsfilename, &input->N, &input->d);

    if(input->s <= 0){
        printf("Invalid value of si parameter!\n");
        exit(1);
    }

    if(input->n <= 0 || input->N % input->n != 0){
        printf("Invalid value of n parameter!\n");
        exit(1);
    }

    input->ns = (int) ceil((double) input->N / (input->s * input->n));

    // Caricamento matrici livelli
    int n, nn;
    input->wq = load_data(wqfilename, &n, &input->nn);

    if(input->d != n){
        printf("Invalid wq size!\n");
        exit(1);
    }

    input->wk = load_data(wkfilename, &n, &nn);

    if(input->d != n || input->nn != nn){
        printf("Invalid wk size!\n");
        exit(1);
    }

    input->wv = load_data(wvfilename, &n, &nn);

    if(input->d != n || input->nn != nn){
        printf("Invalid wv size!\n");
        exit(1);
    }

    // Caricamento bias
    input->bq = load_data(bqfilename, &n, &nn);

    if(n != 1 || input->nn != nn){
        printf("Invalid bq size!\n");
        exit(1);
    }

    input->bk = load_data(bkfilename, &n, &nn);

    if(n != 1 || input->nn != nn){
        printf("Invalid bk size!\n");
        exit(1);
    }

    input->bv = load_data(bvfilename, &n, &nn);

    if(n != 1 || input->nn != nn){
        printf("Invalid bv size!\n");
        exit(1);
    }

    input->out = alloc_matrix(input->N, input->nn);

    //
    // Visualizza il valore dei parametri
    //

    if(!input->silent){
        printf("Dataset file name: '%s'\n", dsfilename);
        printf("WQ file name: '%s'\n", wqfilename);
        printf("WK file name: '%s'\n", wkfilename);
        printf("WV file name: '%s'\n", wvfilename);
        printf("bQ file name: '%s'\n", bqfilename);
        printf("bK file name: '%s'\n", bkfilename);
        printf("bV file name: '%s'\n", bvfilename);
        printf("Dataset row number: %d\n", input->N);
        printf("Tensor first dimention: %d\n", input->s);
        printf("Tensor second dimention: %d\n", input->n);
        printf("Tensor third dimention: %d\n", input->d);
        printf("Dataset block number: %d\n", input->ns);
        printf("Layer neuron number: %d\n", input->nn);
    }

    //
    // Attention Mechanism
    //

    t = clock();
    att(input);
    t = clock() - t;
    time = ((float)t)/CLOCKS_PER_SEC;

    if(!input->silent)
        printf("ATT time = %.3f secs\n", time);
    else
        printf("%.3f\n", time);

    //
    // Salva il risultato
    //
    sprintf(fname, "out32omp_%d_%d_%d_%d.ds2", input->N, input->s, input->n, input->d);
    save_data(fname, input->out, input->N, input->nn);
    if(input->display){
        if(input->out == NULL)
            printf("out: NULL\n");
        else{
            int i,j;
            printf("out: [");
            for(i=0; i<input->N; i++){
                for(j=0; j<input->nn-1; j++)
                    printf("%f,", input->out[input->d*i+j]);
                printf("%f\n", input->out[input->d*i+j]);
            }
            printf("]\n");
        }
    }

    if(!input->silent)
        printf("\nDone.\n");

    return 0;
}
