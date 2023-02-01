; ---------------------------------------------------------
; Regressione con istruzioni SSE a 32 bit
; ---------------------------------------------------------
; F. Angiulli
; 23/11/2017
;

;
; Software necessario per l'esecuzione:
;
;     NASM (www.nasm.us)
;     GCC (gcc.gnu.org)
;
; entrambi sono disponibili come pacchetti software 
; installabili mediante il packaging tool del sistema 
; operativo; per esempio, su Ubuntu, mediante i comandi:
;
;     sudo apt-get install nasm
;     sudo apt-get install gcc
;
; potrebbe essere necessario installare le seguenti librerie:
;
;     sudo apt-get install lib32gcc-4.8-dev (o altra versione)
;     sudo apt-get install libc6-dev-i386
;
; Per generare file oggetto:
;
;     nasm -f elf32 fss32.nasm 
;
%include "sseutils32.nasm"

section .data			; Sezione contenente dati inizializzati
align 16
due:         dd      2.0, 2.0, 2.0, 2.0
align 16
unmezzo:     dd      0.5, 0.5, 0.5, 0.5
align 16
uno:         dd      1.0, 1.0, 1.0, 1.0
align 16
menouno:     dd     -1.0, -1.0, -1.0, -1.0
align 16
negativo:    dd      0x7fffffff, 0x7fffffff, 0x7fffffff, 0x7fffffff

section .bss			; Sezione contenente dati non inizializzati
A:		resb	4
T:		resb	4
B:		resb    4
C:		resb    4
ra:     resb    4
ca:     resb    4
cb:     resb    4

section .text			; Sezione contenente il codice macchina


; ----------------------------------------------------------
; macro per l'allocazione dinamica della memoria
;
;	getmem	<size>,<elements>
;
; alloca un'area di memoria di <size>*<elements> bytes
; (allineata a 16 bytes) e restituisce in EAX
; l'indirizzo del primo bytes del blocco allocato
; (funziona mediante chiamata a funzione C, per cui
; altri registri potrebbero essere modificati)
;
;	fremem	<address>
;
; dealloca l'area di memoria che ha inizio dall'indirizzo
; <address> precedentemente allocata con getmem
; (funziona mediante chiamata a funzione C, per cui
; altri registri potrebbero essere modificati)

extern get_block
extern free_block

%macro	getmem	2
	mov	eax, %1
	push	eax
	mov	eax, %2
	push	eax
	call	get_block
	add	esp, 8
%endmacro

%macro	fremem	1
	push	%1
	call	free_block
	add	esp, 4
%endmacro

global get_matrix_ott:
get_matrix_ott:
    push		ebp		    ; salva il Base Pointer
    mov		    ebp, esp	; il Base Pointer punta al Record di Attivazione corrente
    push		ebx		    ; salva i registri da preservare
    push		esi
    push		edi

    mov ebx, [ebp + 8]	     ; tensore
    mov eax, [ebp + 12]      ; matrice out
    mov ecx, [ebp + 16]      ; start
    mov edx, [ebp + 20]      ; end

    xor edi, edi
    ciclo:
        movaps xmm0, [ebx + ecx * 4]
        movaps [eax + edi], xmm0
        ;--------------------------------
        movaps xmm0, [ebx + ecx * 4 + 16]
        movaps [eax + edi + 16], xmm0
        ;--------------------------------
        movaps xmm0, [ebx + ecx * 4 + 32]
        movaps [eax + edi + 32], xmm0
        ;--------------------------------
        movaps xmm0, [ebx + ecx * 4 + 48]
        movaps [eax + edi + 48], xmm0
        ;--------------------------------
        add ecx, 16
        add edi, 64
        cmp ecx, edx
    jb ciclo

    pop	edi		    ; ripristina i registri da preservare
    pop	esi
    pop	ebx
    mov	esp, ebp	; ripristina lo Stack Pointer
    pop	ebp		    ; ripristina il Base Pointer
    ret			    ; torna alla funzione C chiamante

global set_matrix_ott:
set_matrix_ott:
    push		ebp		    ; salva il Base Pointer
    mov		    ebp, esp	; il Base Pointer punta al Record di Attivazione corrente
    push		ebx		    ; salva i registri da preservare
    push		esi
    push		edi

    mov ebx, [ebp + 8]	     ; matrice
    mov eax, [ebp + 12]      ; tensore out
    mov ecx, [ebp + 16]      ; start
    mov edx, [ebp + 20]      ; end

    xor edi, edi
    ciclo2:
        movaps xmm0, [ebx + edi]
        movaps [eax + ecx * 4], xmm0
        ;--------------------------------
        movaps xmm0, [ebx + edi + 16]
        movaps [eax + ecx * 4 + 16], xmm0
        ;--------------------------------
        movaps xmm0, [ebx + edi + 32]
        movaps [eax + ecx * 4 + 32], xmm0
        ;--------------------------------
        movaps xmm0, [ebx + edi + 48]
        movaps [eax + ecx * 4 + 48], xmm0
        ;--------------------------------
        add ecx, 16
        add edi, 64
        cmp ecx, edx
    jb ciclo2

    pop	edi		    ; ripristina i registri da preservare
    pop	esi
    pop	ebx
    mov	esp, ebp	; ripristina lo Stack Pointer
    pop	ebp		    ; ripristina il Base Pointer
    ret			    ; torna alla funzione C chiamante

global divisione_tensore_ott
divisione_tensore_ott:
    push		ebp		    ; salva il Base Pointer
    mov		    ebp, esp	; il Base Pointer punta al Record di Attivazione corrente
    push		ebx		    ; salva i registri da preservare
    push		esi
    push		edi

    mov esi, [ebp + 12]     ;dimS
    getmem 4, esi           ;ret in eax
    shr esi, 4              ;dimS/16
    mov ecx, [ebp + 8]	    ; tensore
    movss xmm0, [ebp + 16]  ; sqrt(d)
    shufps xmm0, xmm0, 00000000b

    xor edi, edi
    for:
            movaps xmm1, [ecx + edi]
            divps xmm1, xmm0
            movaps [eax + edi], xmm1
            ;--------------------------------
            movaps xmm1, [ecx + edi + 16]
            divps xmm1, xmm0
            movaps [eax + edi + 16], xmm1
            ;--------------------------------
            movaps xmm1, [ecx + edi + 32]
            divps xmm1, xmm0
            movaps [eax + edi + 32], xmm1
            ;--------------------------------
            movaps xmm1, [ecx + edi + 48]
            divps xmm1, xmm0
            movaps [eax + edi + 48], xmm1
            ;--------------------------------
            add edi, 64
            dec esi
    jnz for

    pop	edi		    ; ripristina i registri da preservare
    pop	esi
    pop	ebx
    mov	esp, ebp	; ripristina lo Stack Pointer
    pop	ebp		    ; ripristina il Base Pointer
    ret			    ; torna alla funzione C chiamante

global f_ott
f_ott:
    push		ebp		; salva il Base Pointer
    mov		ebp, esp	; il Base Pointer punta al Record di Attivazione corrente
    push		ebx		; salva i registri da preservare
    push		esi
    push		edi

    mov esi, [ebp + 12]
    getmem 4, esi        ; ret in eax
    shr esi, 4

    mov ecx, [ebp + 8]	     ; tensore
    xor edi, edi             ; i = 0
    fori:
            movaps xmm0, [ecx + edi] ;T[i..i+3]
            addps xmm0, [due]        ;T[i..i+3] + 2
            movaps xmm7, [menouno]   ;-1
            divps xmm7, xmm0         ;-1/(T[i..i+3] + 2)
            addps xmm7, [unmezzo]    ;-1/(T[i..i+3] + 2) + 1/2

            movaps xmm2, [negativo]  ;0x7fffffff
            movaps xmm1, xmm0        ;T[i..i+3] + 2
            andps xmm1, xmm2         ;sgn(T[i..i+3] + 2)
            divps xmm0, xmm1         ;|T[i..i+3] + 2|
            mulps xmm7, xmm0         ;(-1/(T[i..i+3] + 2) + 1/2)*|T[i..i+3] + 2|

            addps xmm7, [unmezzo]    ;(-1/(T[i..i+3] + 2) + 1/2)*s(T[i..i+3])
            movaps [eax + edi], xmm7
            ;----------------------------
            movaps xmm0, [ecx + edi + 16]
            addps xmm0, [due]
            movaps xmm7, [menouno]
            divps xmm7, xmm0
            addps xmm7, [unmezzo]

            movaps xmm2, [negativo]
            movaps xmm1, xmm0
            andps xmm1, xmm2
            divps xmm0, xmm1
            mulps xmm7, xmm0

            addps xmm7, [unmezzo]
            movaps [eax + edi + 16], xmm7
            ;----------------------------
            movaps xmm0, [ecx + edi + 32]
            addps xmm0, [due]
            movaps xmm7, [menouno]
            divps xmm7, xmm0
            addps xmm7, [unmezzo]

            movaps xmm2, [negativo]
            movaps xmm1, xmm0
            andps xmm1, xmm2
            divps xmm0, xmm1
            mulps xmm7, xmm0

            addps xmm7, [unmezzo]
            movaps [eax + edi + 32], xmm7
            ;----------------------------
            movaps xmm0, [ecx + edi + 48]
            addps xmm0, [due]
            movaps xmm7, [menouno]
            divps xmm7, xmm0
            addps xmm7, [unmezzo]

            movaps xmm2, [negativo]
            movaps xmm1, xmm0
            andps xmm1, xmm2
            divps xmm0, xmm1
            mulps xmm7, xmm0

            addps xmm7, [unmezzo]
            movaps [eax + edi + 48], xmm7
            ;----------------------------
            add edi, 64
            dec esi
    jnz fori

    pop	edi		    ; ripristina i registri da preservare
    pop	esi
    pop	ebx
    mov	esp, ebp	; ripristina lo Stack Pointer
    pop	ebp		    ; ripristina il Base Pointer
    ret			    ; torna alla funzione C chiamante


global prodotto_matrici_ott
prodotto_matrici_ott:
    push		ebp		    ; salva il Base Pointer
    mov		    ebp, esp	; il Base Pointer punta al Record di Attivazione corrente
    push		ebx		    ; salva i registri da preservare
    push		esi
    push		edi

    mov ecx, [ebp + 8] ;A
    mov [A], ecx
    mov ecx, [ebp + 20] ;B
    mov [B], ecx
    mov ecx, [ebp + 16] ;ca
    mov [ca], ecx
    mov ecx, [ebp + 12] ;ra
    mov [ra], ecx
    mov ecx, [ebp + 24] ;cb
    mov [cb], ecx

    imul ecx, [ebp + 12]
    getmem 4, ecx
    mov [C], eax

    xor edi, edi ;i
    loop1:
        xor esi, esi ;j
        loop2:
            xor ebx, ebx ;k
            mov eax, edi
            imul eax, [cb] ;i*cb
            add eax, esi   ;i*cb + j
            mov edx, [C]
            movaps xmm0, [edx + eax] ;xmm0 -> ret[i*cb + j,...,i*cb + j + 3]
            loop3:
                mov eax, edi
                imul eax, [ca] ;i*ca
                add eax, ebx   ;i*ca + k
                mov edx, [A]
                movss xmm1, [edx + eax] ;A[i*ca + k]
                shufps xmm1, xmm1, 00000000b ;replico A[i*ca + k] in xmm1

                mov eax, ebx
                imul eax, [cb] ;k*cb
                add eax, esi   ;k*cb + j
                mov edx, [B]
                movaps xmm2, [edx + eax] ;B[k*cb + j,..., k*cb + j + 3]

                mulps xmm2, xmm1 ;B[k*cb + j,..., k*cb + j + 3] * A[i*ca + k, ...]
                addps xmm0, xmm2 ;somma parziale in xmm0

                add ebx, 4
                mov eax, [ca]
                imul eax, 4
                cmp ebx, eax
            jb loop3

            mov eax, edi
            imul eax, [cb]
            add eax, esi
            mov edx, [C]
            movaps [edx + eax], xmm0 ;somma parziale in ret[i*cb + j,...,i*cb + j + 3]

            add esi, 16
            mov eax, [cb]
            imul eax, 4
            cmp esi, eax
        jb loop2

        add edi, 4
        mov eax, [ra]
        imul eax, 4
        cmp edi, eax
    jb loop1
    mov eax, [C]

    pop	edi		    ; ripristina i registri da preservare
    pop	esi
    pop	ebx
    mov	esp, ebp	; ripristina lo Stack Pointer
    pop	ebp		    ; ripristina il Base Pointer
    ret			    ; torna alla funzione C chiamante