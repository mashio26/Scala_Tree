sealed trait Tree[+A] {

  def size: Int = this match {
    // La taille d'une feuille est 1
    case Leaf(value) => 1

    // La taille de la branche c'est la taille de l'arbre gauche plus l'arbre droit plus lui même
    case Branch(left, right) => left.size + right.size + 1
  }

  def count: Int = this match {
    // La taille d'une feuille est 1
    case Leaf(value) => 1

    // Le nombre de feuilles c'est le nombre de feuilles de l'arbre gauche plus l'arbre droit
    case Branch(left, right) => left.count + right.count
  }

  def max(f: (A, A) => Boolean): A = this match {
    // Retourne la valeur de la feuille directement
    case Leaf(value) => value

    // Vérifie dans quelle partie on trouve la plus grande valeur
    case Branch(left,right) => {
      if (f(left.max(f), right.max(f)))
        left.max(f)
      else
        right.max(f)
    }
  }

  def find(f: A => Boolean): Option[A] = this match {
    // Retourne la valeur de la feuille directement
    case Leaf(value) if ( f(value) ) => Some(value)
    // On regarde d'abord la partie droite si on trouve la valeur demander sinon on passe à la droite
    case Branch(left, right) => left.find(f).orElse(right.find(f))
    // Il me faut un cas général alors ici on renvoie une option avec aucune valeur
    case _ => None
  }

  def depth: Int = this match {
    // profondeur d'une feuille est 1
    case Leaf(value) => 1
    // on vérifie si la profondeur de la partie gauche est supérieur à la partie droite
    case Branch(left, right) if (left.depth > right.depth) => left.depth + 1
    // on retourne la profondeur de la partie droite
    case Branch(_, right) => right.depth +1
  }

  def map[B](f:A => B):Tree[B] = this match {
    // on transforme notre valeur de la feuille en passant par la fonction
    case Leaf(value) => Leaf(f(value))
    // on parcoure la partie gauche et droite en appelant la même fonction c'est à dire map
    case Branch(left, right) => Branch(left.map(f), right.map(f))
  }

  def fold[B](fFeuille: A => B)(fBranch: (B, B) => B): B = this match {
    // on applique la fonction de la feuille sur la feuille
    case Leaf(value) => fFeuille(value)
    // on continue le parcour lorsqu'on est sur une branch
    case Branch(left, right) => fBranch(left.fold(fFeuille)(fBranch), right.fold(fFeuille)(fBranch))
  }

  def size2: Int = fold(_ => 1)((left, right) => left + right + 1 )

  def count2: Int = fold(_ => 1)((left, right) => left + right)

  def max2(f: (A, A) => Boolean): A = fold(a => a)((left, right) => if (f(left,right)) left else right)

  def depth2: Int = fold(_ => 1)((left, right) => if (left > right) left + 1 else right + 1)

  def map2[B](f:A => B): Tree[B] = fold(value => Leaf(f(value)))((left : Tree[B], right : Tree[B]) => Branch(left,right))

  def toList: List[A] = {
    // création d'une liste pour faire le parcour
    def loop(t: Tree[A]): List[A] = t match {
      // création des list sur les feuilles
      case Leaf(value) => List(value)
      // concaténation des lists de la partie gauche et la partie droite
      case Branch(left, right) => loop(left) ++ loop(right)
    }

    loop(this)
  }

  def rand(rng : RNG): A = {
    // génération du nombre aléatoire
    val (random :Int, newRng :RNG) = rng.nextInt
    // ici on fait un modulo sur le nombre de feuilles de notre arbre pour obtenir un index
    val selectLeaf = random % count2
    // on transforme notre arbre en list
    val listTree = toList
    // on retourne la valeur de la feuille à l'index généré avant
    listTree(selectLeaf)
  }

}

case class Leaf[A](value : A) extends Tree[A] {}
case class Branch[A](left: Tree[A], right:Tree[A]) extends Tree[A] {}

object Tree {

  // Fonction pour déterminer la valeur maximal dans un arbre
  def max(t: Tree[Int]): Int = t match{

    // Dans le cas d'une feuille on retourne la valeur de celle ci pour la comparer
    case Leaf(value) => value

    // Dans le cas d'une Branch on compare les valeurs possibles entre left et right si ce
    // ne sont pas des feuilles on continu de parcourir l'arbre

    // Dans ce case la c'est le cas ou le left est supérieur au right
    case Branch(left, right) if ( this.max(left) > this.max(right) ) => max(left)

    // Ici l'inverse le right supérieur ou alors les deux sont égaux dans ce cas la j'ai décidé
    // de prendre le right on pourrait faire l'inverse mais ça change rien à mon sens
    case Branch(_, right) => this.max(right)
  }

}

object TestTree {
  import Tree._

  def main(args: Array[String]): Unit = {
    println("Je fais mes tests sur l'arbre suivant : ")

    val arbre = Branch(Branch(Leaf(8), Leaf(16)), Branch(Leaf(120), Leaf(3)))

    val arbreDepth = Branch(Branch(Leaf(8), Leaf(16)), Branch(Branch(Leaf(120),Leaf(10)), Leaf(3)))

    val arbreSolo = Leaf(10)
    println(arbreSolo)

    println(arbre)

    println("Voici la taille de l'arbre : ")
    println(arbre.size)

    println("Voici le nombre de feuille : ")
    println(arbre.count)

    val tree = Tree
    println("Voici la valeur max de l'arbre en utilisant la fonction : ")
    println(tree.max(arbre))

    val arbreString = Branch(Branch(Leaf("Test"), Leaf("T")), Branch(Leaf("Here MAX"), Leaf("Hellosssssssssssssssss")))
    println("Voici le string de taille maximal de l'arbre en utilisant la methode max : ")
    println(arbreString.max((a : String,b : String) => if (a.length >= b.length) true else false))

    println("J'utilise la fonction find (je cherche 3) : ")
    println(arbre.find(a => a == 3))

    println("J'utilise la fonction find dans le cas d'une valeur qui n'existe pas : ")
    println(arbre.find(a => a == 0))

    println("Fonction depth : ")
    println(arbreDepth.depth)

    println("Fonction map j'ajoute 1 à tout l'arbre initial : ")
    println(arbre.map((a) => a + 1))

    println("Fonction size avec le fold : ")
    println(arbre.size2)

    println("Fonction count avec le fold : ")
    println(arbre.count2)

    println("Voici le string de taille maximal de l'arbre en utilisant la methode max avec le fold : ")
    println(arbreString.max2((a: String, b: String) => if (a.length >= b.length) true else false))

    println("Fonction depth avec le fold : ")
    println(arbreDepth.depth2)

    println("Fonction map avec le fold j'ajoute 1 à tout l'arbre initial : ")
    println(arbre.map2((a) => a + 1))

    println("Fonction toList : ")
    println(arbre.toList)

    println("Fonction random : ")
    println(arbre.rand(SimpleRNG(110)))
  }

}
