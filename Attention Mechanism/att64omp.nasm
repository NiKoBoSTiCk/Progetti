; ---------------------------------------------------------
; Regression con istruzioni AVX a 64 bit
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
;     nasm -f elf64 regression64.nasm
;

%include "sseutils64.nasm"

section .data			; Sezione contenente dati inizializzati
align 32
due:         dq      2.0, 2.0, 2.0, 2.0
align 32
unmezzo:     dq      0.5, 0.5, 0.5, 0.5
align 32
uno:         dq      1.0, 1.0, 1.0, 1.0
align 32
menouno:     dq     -1.0, -1.0, -1.0, -1.0
align 32
negativo:    dq      0x7fffffffffffffff, 0x7fffffffffffffff, 0x7fffffffffffffff, 0x7fffffffffffffff

section .bss			; Sezione contenente dati non inizializzati
A:		resb	8
T:		resb	8
B:		resb    8
C:		resb    8
ra:     resb    8
ca:     resb    8
cb:     resb    8

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
	mov	rdi, %1
	mov	rsi, %2
	call	get_block
%endmacro

%macro	fremem	1
	mov	rdi, %1
	call	free_block
%endmacro


global get_matrix_ott:
get_matrix_ott:
	push	rbp				; salva il Base Pointer
	mov		rbp, rsp		; il Base Pointer punta al Record di Attivazione corrente

    ;rdi tensore
    ;rsi out
    ;rdx start
    ;rcx end
    mov rax, rsi    ;out
    xor rbx, rbx
    ciclo:
        vmovapd ymm0, [rdi + rdx*8]
        vmovapd [rax + rbx], ymm0
        ;-------------------------------
        vmovapd ymm0, [rdi + rdx*8 + 32]
        vmovapd [rax + rbx + 32], ymm0
        ;-------------------------------
        vmovapd ymm0, [rdi + rdx*8 + 64]
        vmovapd [rax + rbx + 64], ymm0
        ;-------------------------------
        vmovapd ymm0, [rdi + rdx*8 + 96]
        vmovapd [rax + rbx + 96], ymm0
        ;-------------------------------
        add rdx, 16
        add rbx, 128
        cmp rdx, rcx
    jb ciclo

 	mov	rsp, rbp	; ripristina lo Stack Pointer
 	pop	rbp		    ; ripristina il Base Pointer
 	ret				; torna alla funzione C chiamante

global set_matrix_ott:
set_matrix_ott:
	push	rbp				; salva il Base Pointer
	mov		rbp, rsp		; il Base Pointer punta al Record di Attivazione corrente

    ;rdi tensore
    ;rsi out
    ;rdx start
    ;rcx end
    mov rax, rsi    ;out
    xor rbx, rbx
    ciclo2:
        vmovapd ymm0, [rdi + rbx]
        vmovapd [rax + rdx*8], ymm0
        ;-------------------------------
        vmovapd ymm0, [rdi + rbx + 32]
        vmovapd [rax + rdx*8 + 32], ymm0
        ;-------------------------------
        vmovapd ymm0, [rdi + rbx + 64]
        vmovapd [rax + rdx*8 + 64], ymm0
        ;-------------------------------
        vmovapd ymm0, [rdi + rbx + 96]
        vmovapd [rax + rdx*8 + 96], ymm0
        ;-------------------------------
        add rdx, 16
        add rbx, 128
        cmp rdx, rcx
    jb ciclo2

 	mov	rsp, rbp	; ripristina lo Stack Pointer
 	pop	rbp		    ; ripristina il Base Pointer
 	ret				; torna alla funzione C chiamante


global divisione_tensore_ott
divisione_tensore_ott:
	push	rbp				; salva il Base Pointer
	mov		rbp, rsp		; il Base Pointer punta al Record di Attivazione corrente

    ;rdi tensore
    ;rsi dimS
    ;xmm0/rax d
    mov rcx, rsi ;getmem usa rsi
    vmovsd xmm1, xmm0

    push rsi
    push rdi
    getmem 8, rcx
    pop rdi
    pop rsi
    shr rsi, 4

    vpbroadcastq ymm0, xmm1 ;replica xmmo in ymmo

    xor rbx, rbx
    for:
        vmovapd ymm1, [rdi + rbx]
        vdivpd ymm1, ymm0
        vmovapd [rax + rbx], ymm1
        ;-----------------------------
        vmovapd ymm1, [rdi + rbx + 32]
        vdivpd ymm1, ymm0
        vmovapd [rax + rbx + 32], ymm1
        ;-----------------------------
        vmovapd ymm1, [rdi + rbx + 64]
        vdivpd ymm1, ymm0
        vmovapd [rax + rbx + 64], ymm1
        ;-----------------------------
        vmovapd ymm1, [rdi + rbx + 96]
        vdivpd ymm1, ymm0
        vmovapd [rax + rbx + 96], ymm1
        ;-----------------------------
        add rbx, 128
        dec rsi
    jnz for

	mov	rsp, rbp	; ripristina lo Stack Pointer
	pop	rbp		    ; ripristina il Base Pointer
	ret				; torna alla funzione C chiamante

global f_ott
f_ott:
	push	rbp				; salva il Base Pointer
	mov		rbp, rsp		; il Base Pointer punta al Record di Attivazione corrente

    ;rdi tensore
    ;rsi dimS
    mov rcx, rsi ;getmem usa rsi
    push rdx
    push rsi
    push rdi
    getmem 8, rcx
    pop rdi
    pop rsi
    pop rdx
    shr rsi, 4

    xor rbx, rbx
    fori:
        vmovapd ymm0, [rdi + rbx]
        vaddpd ymm0, [due]
        vmovapd ymm7, [menouno]
        vdivpd ymm7, ymm0
        vaddpd ymm7, [unmezzo]

        vmovapd ymm2, [negativo]
        vmovapd ymm1, ymm0
        vandpd ymm1, ymm2
        vdivpd ymm0, ymm1
        vmulpd ymm7, ymm0

        vaddpd ymm7, [unmezzo]
        vmovapd [rax + rbx], ymm7
        ;-----------------------------
        vmovapd ymm0, [rdi + rbx + 32]
        vaddpd ymm0, [due]
        vmovapd ymm7, [menouno]
        vdivpd ymm7, ymm0
        vaddpd ymm7, [unmezzo]

        vmovapd ymm2, [negativo]
        vmovapd ymm1, ymm0
        vandpd ymm1, ymm2
        vdivpd ymm0, ymm1
        vmulpd ymm7, ymm0

        vaddpd ymm7, [unmezzo]
        vmovapd [rax + rbx + 32], ymm7
        ;-----------------------------
        vmovapd ymm0, [rdi + rbx + 64]
        vaddpd ymm0, [due]
        vmovapd ymm7, [menouno]
        vdivpd ymm7, ymm0
        vaddpd ymm7, [unmezzo]

        vmovapd ymm2, [negativo]
        vmovapd ymm1, ymm0
        vandpd ymm1, ymm2
        vdivpd ymm0, ymm1
        vmulpd ymm7, ymm0

        vaddpd ymm7, [unmezzo]
        vmovapd [rax + rbx + 64], ymm7
        ;-----------------------------
        vmovapd ymm0, [rdi + rbx + 96]
        vaddpd ymm0, [due]
        vmovapd ymm7, [menouno]
        vdivpd ymm7, ymm0
        vaddpd ymm7, [unmezzo]

        vmovapd ymm2, [negativo]
        vmovapd ymm1, ymm0
        vandpd ymm1, ymm2
        vdivpd ymm0, ymm1
        vmulpd ymm7, ymm0

        vaddpd ymm7, [unmezzo]
        vmovapd [rax + rbx + 96], ymm7
        ;-----------------------------
        add rbx, 128
        dec rsi
    jnz fori

	mov	rsp, rbp	; ripristina lo Stack Pointer
	pop	rbp		    ; ripristina il Base Pointer
	ret				; torna alla funzione C chiamante


global prodotto_matrici_ott
prodotto_matrici_ott:
 	push	rbp				; salva il Base Pointer
 	mov		rbp, rsp		; il Base Pointer punta al Record di Attivazione corrente
    ;salvo i parametri
    mov [A], rdi
    mov [ra], rsi
    mov [ca], rdx
    mov [B], rcx
    mov [cb], r8

    mov rcx, [ra]
    imul rcx, [cb]
    getmem 8, rcx ; ret in rax
    mov [C], rax

    xor rdi, rdi ;i
    loop1:
        xor rsi, rsi ;j
        loop2:
            xor rbx, rbx ;k
            mov rax, rdi
            imul rax, [cb]
            add rax, rsi
            mov rdx, [C]
            vmovapd ymm0, [rdx + rax]
            loop3:
                mov rax, rdi
                imul rax, [ca]
                add rax, rbx
                mov rdx, [A]
                vmovsd xmm1, [rdx + rax]
                vpbroadcastq ymm1, xmm1

                mov rax, rbx
                imul rax, [cb]
                add rax, rsi
                mov rdx, [B]
                vmovapd ymm2, [rdx + rax]

                vmulpd ymm2, ymm1
                vaddpd ymm0, ymm2

                add rbx, 8
                mov rax, [ca]
                imul rax, 8
                cmp rbx, rax
            jb loop3

            mov rax, rdi
            imul rax, [cb]
            add rax, rsi
            mov rdx, [C]
            vmovapd [rdx + rax], ymm0

            add rsi, 32
            mov rax, [cb]
            imul rax, 8
            cmp rsi, rax
        jb loop2

        add rdi, 8
        mov rax, [ra]
        imul rax, 8
        cmp rdi, rax
    jb loop1
    mov rax, [C]

 	mov	rsp, rbp	; ripristina lo Stack Pointer
 	pop	rbp		    ; ripristina il Base Pointer
 	ret				; torna alla funzione C chiamante