# Tema **PDraw**, grupo **pdraw-t11**
-----

## Constituição dos grupos e participação individual global

| NMec | Nome | Participação |
|:---:|:---|:---:|
| 102954 | DIOGO MARTINS NUNES MAIA BORGES | 15.0% |
| 107961 | GUILHERME SILVA SANTOS | 23.3% |
| 108180 | HUGO MIGUEL XAVIER RODRIGUES | 23.3% |
| 104327 | ISAAC DAVID DELGADO SANTIAGO | 15.0% |
| 107708 | JOÃO PEDRO PEREIRA GASPAR | 23.3% |

## Relatório

- Uma vez que o ipdraw não se encontra funcional, a execução do resultado da compilação do p1.pdraw não funciona. Criámos um temp.pdraw de modo a mostrar que efetivamente a sua compilação resulta num ficheiro python executável. Relativamente ao p2.pdraw o mesmo encontra-se funcional.
  
- Como correr:
  ```
  antlr4-build -python ipdrawGrammar
  ```
  ```
  antlr4-build pdrawGrammar
  ```
  ```
  cat ../examples/p1.pdraw | antlr4-run > ../python/pdraw1.py
  ```
  ```
  python3 ../python/pdraw1.py
  ```
  ```
  cat ../examples/p2.pdraw | antlr4-run > ../python/pdraw2.py
  ```
  ```
  python3 ../python/pdraw2.py
  ```
  ```
  cat ../examples/temp.pdraw | antlr4-run > ../python/pdrawtemp.py
  ```
  ```
  python3 ../python/pdrawtemp.py
  ```

## Contribuições

O projeto foi desenvolvido, dividindo o grupo em 2 sub-grupos:

  - GrupoA (Guilherme Santos, Hugo Rodrigues e Joao Gaspar):

    Gramáticas do pdraw1, pdraw2, pdraw3 e ipdraw.
    
    Foi criado o Compiler.java para a linguagem principal e um de python para a linguagem secundária (ipdrawGrammar.py).
    
    Desenvolvimento da maioria dos pormenores relacionados com os requesitos minimos (+ gramática do pdraw3).

  - GrupoB (Isaac Santiago e Diogo Borges):
    
    pequenas alteraçẽos no Compiler.java
    
    iCompiler.java (acabou por ser desnecessário após a criação da gramática do ipdrawGrammar.py)
    
    ipdrawGrammarMain.java (acabou por ser desnecessário após a criação da gramática do ipdrawGrammar.py)

