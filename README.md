# 🌳 Projet : Implémentation d’un Arbre en Scala
## 📌 Présentation

Ce projet implémente une structure de données arborescente générique (Tree) en Scala, accompagnée de plusieurs méthodes classiques et avancées pour manipuler et explorer un arbre.

## 🚀 Fonctionnalités

L’implémentation fournit :

 - Calcul de propriétés :

    - size → taille de l’arbre (nombre de nœuds)

    - count → nombre de feuilles

    - depth → profondeur de l’arbre

 - Manipulation et exploration :

    - map → transformation des valeurs

    - find → recherche d’un élément selon un prédicat

    - max → valeur maximale selon un comparateur

    - toList → conversion de l’arbre en liste

 - Version fonctionnelle via fold :

    - size2, count2, max2, depth2, map2

 - Autres utilitaires :

    - rand → sélection aléatoire d’une feuille

 - Tests d’exemple dans TestTree

## 🛠️ Technologies

Langage : Scala

Paradigme : Programmation fonctionnelle (pattern matching, récursion, fold)

## 📂 Fichiers

Tree.scala → définition de la structure (Tree, Leaf, Branch) et méthodes associées.

TestTree → programme principal contenant des exemples d’utilisation et tests simples.

## 🎯 Objectif pédagogique

Ce projet est avant tout un exercice d’apprentissage en Scala, permettant de manipuler :

Le pattern matching

Les types algébriques (sealed trait, case class)

Les fonctions d’ordre supérieur (map, fold)

La représentation récursive d’un arbre binaire