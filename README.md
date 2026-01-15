This project was carried out by a team of 5, from September 2024 to December 2024. It was forked from an existing private repository.

# Instructions d'utilisation de l'application

## 1- Lancement du logiciel
Pour lancer le logiciel, il faut cliquer sur le fichier .jar

## 2- Ouverture du logiciel
Au lancement du logociel, une interface avec plusieurs boutons est affichée à l'utilisateur.  

Les boutons de coupe sont les suivants: 
> Ajouter une coupe rectengulaire  
> Ajouter une coupe en L  
> Ajouter une couoe parallèle  
> Ajouter une coupe en bordure

Pour le panneau:
> Gérer le panneau
> Rénitialiser le zoom

Pour la liste des objets constituant la CNC: 
> Gérer les outils  
> Gérer les coupes  
> Gérer les zones interdites  

## 3- Panneau
Avant de pouvoir créer et modifier des coupes, il faut créer un panneau.  
Pour ce faire, il faut cliquer le bouton << Gérer le panneau >>et: 

&nbsp; 1- Indiquer la largeur du panneau désiré en mm  
&nbsp; 2- Indiquer la hauteur du panneau désiré en mm  
&nbsp; 3- Indiquer la profondeur du panneau désiré en mm  
&nbsp; 4- Cliquer sur << Appliquer >>


## 4- Pour ajouter une coupe
### 4.1- Ajouter une coupe rectengulaire
Lorsqu'on clique sur le bouton ajouter une coupe rectengulaire:  

&nbsp; 1- On clique d'abord sur un endoit du panneau pour indiquer le point de référence.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Ce point peut être n'importe où sur le panneau.  
&nbsp; 2- On clique ensuite deux autres fois sur le panneau pour former la coupe rectengulaire désirée.

### 4.2- Ajouter une coupe en L
Lorsqu'on clique sur le bouton ajouter une coupe en L:  

&nbsp; 1- On clique d'abord sur un autre endroit du panneau pour définir le coin de la coupe en L.  
&nbsp; 2- On clique ensuite sur un endoit du panneau pour indiquer le point de référence.    
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Ce point ne peut pas être n'importe où sur le panneau. Il doit être une intersection valide.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Coin du panneau, inetersection entre deux coupes, coin d'une coupe en bordure, coin d'une coupe rectengulaire, coin d'une coupe en L.  

**IMPORTANT** Lorsqu'on choisi une intersection, il est recommendé de faire un zoom sur l'intersection et de la cliquer ensuite pour s'assurer de cliquer sur un point valide.

### 4.3- Ajouter une coupe en parallèle

Lorsqu'on clique sur le bouton ajouter une coupe parallèle:

&nbsp; 1- On indique une taille dans le champ de texte pour spécifier la taille après la coupe et on sort du champ pour confirmer cette valeur.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; La taille peut être soit positive ou négative.   
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Positif si on veut la coupe à droite ou en haut de la ligne de référence.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Négatif si on veut la coupe à gauche ou en bas de la ligne de référence.  
&nbsp; 2- On clique ensuite sur la ligne de référence.

### 4.4- Ajouter une coupe en bordure

Lorsqu'on clique sur le bouton ajouter une coupe parallèle:

&nbsp; 1- On indique la largeur dans le champ de texte.  
&nbsp; 2- On indique la hauteur dans le champ de texte.  
&nbsp; 3- On clique sur le bouton << Appliquer >>.

**Remarque:** On peut voir que l'outil et la profondeur ne sont pas modifiables à l'ajout d'une coupe. Il faudra donc d'abord créer la coupe et ensuite la modifier.

## 5- Modifications des coupes

Pour afficher le menu de modification d'une coupe, il va falloir cliquer sur la coupe.  
La coupe séléctionnée sera en vert.  
Les coupes invalides après la modification seront en rouge.  

Pour modifier la profondeur; On met la profondeur désiré dans le champ de texte et on sort de celle-ci pour confirmer.  
Pour modifier l'outil; On clique sur le menu déroulant et on clique sur l'outil désiré.

### 5.1- Modification de la coupe rectengulaire et en L

Une fois sur le menu de modifications, on peut modifier de plusieurs manières les points de ces coupes.

**Par champ de texte**  
&nbsp; 1- On indique la nouvelle valeur souhaité dans le champ de texte.  
&nbsp; 2- On confirme le changement en sortant de la boite de texte.  
**Par clique**  
&nbsp; 1- On clique d'abord sur le bouton du point qu'on veut modifier.  
&nbsp; 2- On clique sur le panneau à l'endroit où on veut le nouveau point.

On peut aussi supprimer la coupe en cliquant sur le bouton << Supprimer >>. 

### 5.1- Modification de la coupe parallèle

Une fois sur le menu de modifications, on peut modifier deux valeur de cette coupe.

**La taille**  
Pour modifier la taille, on indique la nouvelle taille dans le champ de texte et on sort de celle-ci pour la confirmer.  
**La ligne de référence**  
Pour moidifier la ligne à laquelle on fait référence, on clique sur le bouton << Référence >> et on clique sur la ligne désirée.

### 5.1- Modification de la coupe en bordure

Une fois sur le menu de modifications, on indique la ou les nouvelles dimensions dans le champ de texte et on clique sur << Appliquer >>.

## 6- Outils

Dans le menu d'outils, on peut créer ou supprimer des outils.

### Pour créer un nouvel outil
&nbsp; 1- On commence par mettre le nom du nouvel outil dans le champ de texte.  
&nbsp; 2- On indique le diamètre du nouvel outil.    
&nbsp; 3- On clique sur << Ajouter un outil >>. 
### Pour supprimer un outil
On clique sur l'outil dans la liste et on clique sur << Supprimer l'outil sélectionné >>. 
