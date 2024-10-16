module t4.gy07 where
open import lib hiding (_+_; _*_; _-_; _<_)

-- List of unicode symbols:
--    →       \to
--            \rightarrow
--    ℕ       \bN           'b'lackboard 'N', there is also \bZ for ℤ, etc
--    λ       \Gl           'G'reek 'l', there is also \GG for Γ, etc
--            \lambda
--    ×       \times
--    ⊎       \u+
--    ⊤       \top
--    ⊥       \bot
--    ↔       \lr
--    ₁       \_1
--    ₐ       \_a
--    ¹       \^1

---------------------------------------------------------
-- natural numbers
---------------------------------------------------------

-- data ℕ : Type where
--   zero : ℕ
--   suc  : ℕ → ℕ    --   suc n  ＝  n+1

three : ℕ
three = suc (suc (suc zero))

three-test : three ≡ 3
three-test = refl

double : ℕ → ℕ
double zero    = zero
double (suc n) = suc (suc (double n))
                          -- 2* (n +1) = (2* n) +2
                          -- double (suc n) = suc (suc (double n))

-- bad : ℕ → ℕ
-- bad zero = zero
-- bad (suc n) = suc (bad (suc (suc n)))
  -- not allowed: suc (suc n) is not smaller than suc n

double-test1 : double 2 ≡ 4
double-test1 = refl
double-test2 : double 0 ≡ 0
double-test2 = refl
double-test3 : double 10 ≡ 20
double-test3 = refl

half : ℕ → ℕ
half zero = 0
half (suc zero) = 0
half (suc (suc n)) = suc (half n)

half-test1 : half 10 ≡ 5
half-test1 = refl
half-test2 : half 11 ≡ 5
half-test2 = refl
half-test3 : half 12 ≡ 6
half-test3 = refl


-- + is the iteration of suc

-- a + b   =   suc (suc (suc ... b
--             \ a times   /

_+_ : ℕ → ℕ → ℕ
zero  + b = b
suc a + b = suc (a + b)
infixl 6 _+_

+-test1 : 3 + 5 ≡ 8
+-test1 = refl
+-test2 : 0 + 5 ≡ 5
+-test2 = refl
+-test3 : 5 + 0 ≡ 5
+-test3 = refl

_*_ : ℕ → ℕ → ℕ
zero  * b = zero
suc a * b = a * b + b
infixl 7 _*_

*-test1 : 3 * 4 ≡ 12
*-test1 = refl
*-test2 : 3 * 1 ≡ 3
*-test2 = refl
*-test3 : 3 * 0 ≡ 0
*-test3 = refl
*-test4 : 0 * 10 ≡ 0
*-test4 = refl

_^_ : ℕ → ℕ → ℕ
a ^ 0 = 1
a ^ suc b = a * a ^ b
infixl 8 _^_

^-test1 : 3 ^ 4 ≡ 81
^-test1 = refl
^-test2 : 3 ^ 0 ≡ 1
^-test2 = refl
^-test3 : 0 ^ 3 ≡ 0
^-test3 = refl
^-test4 : 1 ^ 3 ≡ 1
^-test4 = refl
^-test5 : 0 ^ 0 ≡ 1
^-test5 = refl

_! : ℕ → ℕ
zero !  = 1
suc n ! = suc n * n !

!-test1 : 3 ! ≡ 6
!-test1 = refl
!-test2 : 1 ! ≡ 1
!-test2 = refl
!-test3 : 6 ! ≡ 720
!-test3 = refl

-- Subtraction of natural numbers
--  /!\  n - m = 0  when m > 0

_-_ : ℕ → ℕ → ℕ
zero - zero = zero
zero - suc m = zero
suc n - zero = suc n
suc n - suc m = n - m
infixl 6 _-_

-test1 : 3 - 2 ≡ 1
-test1 = refl
-test2 : 3 - 3 ≡ 0
-test2 = refl
-test3 : 3 - 4 ≡ 0
-test3 = refl

-- Comparison functions
_≥_ : ℕ → ℕ → Bool
zero ≥ zero = true
zero ≥ suc m = false
suc n ≥ zero = true
suc n ≥ suc m = n ≥ m

≥test1 : 3 ≥ 2 ≡ true
≥test1 = refl
≥test2 : 3 ≥ 3 ≡ true
≥test2 = refl
≥test3 : 3 ≥ 4 ≡ false
≥test3 = refl

_>_ : ℕ → ℕ → Bool
n > m = n ≥ suc m

>test1 : 3 > 2 ≡ true
>test1 = refl
>test2 : 3 > 3 ≡ false
>test2 = refl
>test3 : 3 > 4 ≡ false
>test3 = refl

_<_ : ℕ → ℕ → Bool
n < m = m > n

<test1 : 3 < 2 ≡ false
<test1 = refl
<test2 : 3 < 3 ≡ false
<test2 = refl
<test3 : 3 < 4 ≡ true
<test3 = refl

min : ℕ → ℕ → ℕ
min zero zero = zero
min zero (suc m) = zero
min (suc n) zero = zero
min (suc n) (suc m) = suc (min n m)

min-test1 : min 3 2 ≡ 2
min-test1 = refl
min-test2 : min 2 3 ≡ 2
min-test2 = refl
min-test3 : min 3 3 ≡ 3
min-test3 = refl

-- We can use the TERMINATING pragma to disable the termination checker.
gcd : ℕ → ℕ → ℕ
{-# TERMINATING #-}
gcd 0 m = m
gcd (suc n) 0 = suc n
gcd (suc n) (suc m) = if n ≥ m then gcd (n - m) (suc m) else gcd (suc n) (m - n)

gcd-test1 : gcd 6 9 ≡ 3
gcd-test1 = refl
gcd-test2 : gcd 100 150 ≡ 50
gcd-test2 = refl
gcd-test3 : gcd 17 19 ≡ 1
gcd-test3 = refl
gcd-test4 : gcd 12 24 ≡ 12
gcd-test4 = refl
gcd-test5 : gcd 19 17 ≡ 1
gcd-test5 = refl

-- Use the same definition as for `gcd`, but with recursion on the first parameter.
--   gcd-helper fuel n m  should be equal to  gcd n m  when fuel is large enough.
gcd-helper : ℕ → ℕ → ℕ → ℕ
gcd-helper zero       n m = 42
gcd-helper (suc fuel) 0 m = m
gcd-helper (suc fuel) (suc n) 0 = suc n
gcd-helper (suc fuel) (suc n) (suc m) = if n ≥ m then gcd-helper fuel (n - m) (suc m) else gcd-helper fuel (suc n) (m - n)

gcd' : ℕ → ℕ → ℕ
gcd' n m = gcd-helper (m + n + 1) n m

gcd'-test1 : gcd' 6 9 ≡ 3
gcd'-test1 = refl
gcd'-test2 : gcd' 100 150 ≡ 50
gcd'-test2 = refl
gcd'-test3 : gcd' 17 19 ≡ 1
gcd'-test3 = refl
gcd'-test4 : gcd' 12 24 ≡ 12
gcd'-test4 = refl
gcd'-test5 : gcd' 19 17 ≡ 1
gcd'-test5 = refl

not : Bool → Bool
not true = false
not false = true

even? : ℕ → Bool
even? zero = true
even? (suc n) = not (even? n)

even?-test1 : even? 3 ≡ false
even?-test1 = refl
even?-test2 : even? 200 ≡ true
even?-test2 = refl

fib : ℕ → ℕ
fib zero = 1
fib (suc zero) = 1
fib (suc (suc n)) = fib (suc n) + fib n

fib-test1 : fib 6 ≡ 13
fib-test1 = refl
fib-test2 : fib 3 ≡ 3
fib-test2 = refl

-- divrem2 a should be a pair (q , r) where q and r are the quotient and remainder of the division of a by 2.
--  The remainder r is encoded as a boolean: 0 corresponds to false and 1 corresponds to true.
divrem2 : ℕ → ℕ × Bool
divrem2 zero = 0 , false
divrem2 (suc n) = let q , r = divrem2 n in (if r then suc q else q) , not r

divrem2-test1 : divrem2 5 ≡ (2 , true)
divrem2-test1 = refl
divrem2-test2 : divrem2 10 ≡ (5 , false)
divrem2-test2 = refl

-- Try to define rem and div without {-# TERMINATING #-} ! You may need some helper functions.
-- rem a b = remainder of the division of a by (suc b).
--  /!\ Since division by zero is not possible, the second argument is shifted by 1 (see the examples).

rem : ℕ → ℕ → ℕ
rem zero b = zero
rem (suc a) b = let r = rem a b
                in if r ≥ b then zero else suc r

rem-test1 : rem 5 1 ≡ 1
rem-test1 = refl
rem-test2 : rem 11 2 ≡ 2
rem-test2 = refl

-- div a b = quotient of the division of a by (suc b)

divrem : ℕ → ℕ → ℕ × ℕ
divrem zero b = zero , zero
divrem (suc a) b = let q , r = divrem a b
                   in if r ≥ b then (suc q , zero) else (q , suc r)

div : ℕ → ℕ → ℕ
div a b = divrem a b .fst

div-test1 : div 5 1 ≡ 2
div-test1 = refl
div-test2 : div 11 2 ≡ 3
div-test2 = refl

-- Iterator (iteNat) and recursor (recNat):

iteNat : {A : Type} → A → (A → A) → ℕ → A
iteNat z s zero = z
iteNat z s (suc n) = s (iteNat z s n)

-- n = suc (suc (suc zero))
-- iteNat z s n    =    s (s (s z))

recNat : {A : Type} → A → (ℕ → A → A) → ℕ → A
recNat z s zero = z
recNat z s (suc n) = s n (recNat z s n)

-- n = suc (suc (suc zero))
-- recNat z s n = s (suc (suc zero)) (s (suc zero) (s zero z))

-- double : ℕ → ℕ
-- double zero    = zero
-- double (suc n) = suc (suc (double n))

double' : ℕ → ℕ
double' n = iteNat
            zero
            (λ doublen → suc (suc doublen))
            n

-- _+_ : ℕ → ℕ → ℕ
-- zero  + b = b
-- suc a + b = suc (a + b)
-- infixl 6 _+_


-- Redefine _+_ and fib using recNat or iteNat:
_+'_ : ℕ → ℕ → ℕ
-- a +' b = iteNat b (λ a+b → suc (a+b)) a
a +' b = iteNat b (λ x → suc x) a

fib' : ℕ → ℕ
fib' n = let result = iteNat {ℕ × ℕ}
                      (1 , 1)
                   -- (λ (fibn , fibn+1) → fibn+1 , fibn + fibn+1)
                      (λ (x , y) → y , x + y)
                      n in
             -- (fib n , fib (n + 1))
         result .fst


-- Redefine iteNat using recNat:
iteNat' : {A : Type} → A → (A → A) → ℕ → A
iteNat' z s n = recNat z (λ _ → s) n

iteNat'-test1 : {A : Type}{z : A}{s : A → A} → iteNat' z s zero ≡ z
iteNat'-test1 = refl
iteNat'-test2 : {A : Type}{z : A}{s : A → A}{n : ℕ} → iteNat' z s (suc n) ≡ s (iteNat' z s n)
iteNat'-test2 = refl

-- Redefine recNat using iteNat:
recNat' : {A : Type} → A → (ℕ → A → A) → ℕ → A
recNat' {A} z s n
  = let result = iteNat {A × ℕ} (z , zero) (λ (x , n) → s n x , suc n) n
    in result .fst

recNat'-test1 : {A : Type}{z : A}{s : ℕ → A → A} → recNat' z s zero ≡ z
recNat'-test1 = refl
recNat'-test2 : {A : Type}{z : A}{s : ℕ → A → A} → recNat' z s 3 ≡ s 2 (s 1 (s 0 z))
recNat'-test2 = refl



---------------------------------------------------------
-- lists
---------------------------------------------------------

data List (A : Type) : Type where
  [] : List A
  _∷_ : A → List A → List A
infixr 6 _∷_

length : {A : Type} → List A → ℕ
length = {!!}

-- length-test1 : length (1 ∷ 2 ∷ 3 ∷ []) ≡ 3
-- length-test1 = refl
-- length-test2 : length (1 ∷ []) ≡ 1
-- length-test2 = refl

sumList : List ℕ → ℕ
sumList = {!!}

-- sumList-test : sumList (1 ∷ 2 ∷ 3 ∷ []) ≡ 6
-- sumList-test = refl

_++_ : {A : Type} → List A → List A → List A
_++_ = {!!}
infixr 5 _++_

-- ++-test : 3 ∷ 2 ∷ [] ++ 1 ∷ 4 ∷ [] ≡ 3 ∷ 2 ∷ 1 ∷ 4 ∷ []
-- ++-test = refl

map : {A B : Type} → (A → B) → List A → List B
map = {!!}

-- map-test : map (_+ 2) (3 ∷ 9 ∷ []) ≡ (5 ∷ 11 ∷ [])
-- map-test = refl

iteList : {A B : Type} → B → (A → B → B) → List A → B
iteList n c [] = n
iteList n c (a ∷ as) = c a (iteList n c as)

-- Redefine some of the above functions (length, ...) using iteList.

---------------------------------------------------------
-- trees
---------------------------------------------------------

-- a datatype of expressions

data Expr : Set where
  const : ℕ → Expr
  _[+]_ : Expr → Expr → Expr
  _[*]_ : Expr → Expr → Expr

-- 2 * (3 + 4) reprezentacioja:
e : Expr
e = const 2 [*] (const 3 [+] const 4)
{-
  *
 / \
2   +
   / \
  3   4
-}

eval : Expr → ℕ
eval = {!!}

-- eval-test : eval e ≡ 14
-- eval-test = refl

height : Expr → ℕ
height = {!!}

-- height-test : height e ≡ 2
-- height-test = refl

-- http://www.cs.nott.ac.uk/~psztxa/mgs.2021/datatypes.pdf -ben a 3. feladat (74. oldal):

data Tree (A : Type) : Type where
  leaf : Tree A
  node : Tree A → A → Tree A → Tree A

t : Tree ℕ
t = node (node leaf 1 (node leaf 2 leaf)) 5 leaf
{-
    5
   / \
  1
 / \
    2
   / \
-}


tree2List : {A : Type} → Tree A → List A
tree2List = {!!}

-- tree2List-test : tree2List t ≡ 1 ∷ 2 ∷ 5 ∷ []
-- tree2List-test = refl

_≤?_ : ℕ → ℕ → Bool
zero ≤? m = true
suc n ≤? zero = false
suc n ≤? suc m = n ≤? m

-- egy fa rendezett, ha minden csomopontnal levo erteknel a bal reszfaban kisebb, a kobb reszfaban pedig nagyobb ertekek vannak. peldaul t rendezett

-- ez a fuggveny egy rendezett faba illeszt be egy uj erteket ugy,
-- hogy a fa rendezett maradjon
insert : ℕ → Tree ℕ → Tree ℕ
insert = {!!}

t' : Tree ℕ
t' = node (node (node leaf 0 leaf) 1 (node leaf 2 leaf)) 5 leaf
{-
      5
     / \
    1
   / \
  0   2
 / \ / \
-}

insert-test : insert 0 t ≡ t'
insert-test = refl

-- egy listat egy rendezett fara alakit.
list2tree : List ℕ → Tree ℕ
list2tree = λ _ → leaf

tree-sort : List ℕ → List ℕ
tree-sort = {!!}

-- tree-sort-test1 : tree-sort (10 ∷ 2 ∷ 1 ∷ 5 ∷ []) ≡ 1 ∷ 2 ∷ 5 ∷ 10 ∷ []
-- tree-sort-test1 = refl

-- tree-sort-test2 : tree-sort (1 ∷ 2 ∷ 1 ∷ 5 ∷ []) ≡ 1 ∷ 1 ∷ 2 ∷ 5 ∷ []
-- tree-sort-test2 = refl

-- nested types

data RoseTree : Type where
  node : List RoseTree → RoseTree

tR : RoseTree
tR = node (node (node [] ∷ []) ∷ node [] ∷ node (node [] ∷ node [] ∷ []) ∷ [])
{-
  /|\
 |  /\
-}

countNodes     : RoseTree → ℕ
countNodesList : List RoseTree → ℕ
countNodes = {!!}
countNodesList = {!!}

countNodes-test : countNodes tR ≡ 7
countNodes-test = refl

-- maximum of two numbers
max : ℕ → ℕ → ℕ
max = {!!}

-- max-test1 : max 3 2 ≡ 3
-- max-test1 = refl
-- max-test2 : max 20 30 ≡ 30
-- max-test2 = refl
-- max-test3 : max 20 20 ≡ 20
-- max-test3 = refl

heightRoseTree : RoseTree → ℕ
heightRoseTreeList : List RoseTree → ℕ
heightRoseTree = {!!}
heightRoseTreeList = {!!}

-- heightRoseTree-test1 : heightRoseTree tR ≡ 2
-- heightRoseTree-test1 = refl
-- heightRoseTree-test2 : heightRoseTree (node (node (node (node [] ∷ []) ∷ []) ∷ [])) ≡ 3
-- heightRoseTree-test2 = refl

-- vegtelenul elagazodo fak (infinitely branching trees)

data TreeInf : Type where
  leaf : TreeInf
  node : (ℕ → TreeInf) → TreeInf

-- a balanced tree which has height two (draw it!)
t2 : TreeInf
t2 = node (λ _ → node (λ _ → leaf))

-- tI n should be a complete tree of height n (all branches should have height n-1, and so on)
tI : ℕ → TreeInf
tI = {!!}

-- tI-test1 : tI 3 ≡ node λ _ → node λ _ → node λ _ → leaf
-- tI-test1 = refl
-- tI-test2 : tI 5 ≡ node λ _ → node λ _ → node λ _ → node λ _ → node λ _ → leaf
-- tI-test2 = refl

-- a tree where the height of the n^th branch is n (all branches have finite length, but there is no upper bound)
tI' : TreeInf
tI' = {!!}

_!_ : TreeInf → ℕ → TreeInf
leaf ! n = leaf
node ts ! n = ts n

-- test-tI'1 : tI' ! 0 ≡ leaf
-- test-tI'1 = refl
-- test-tI'2 : tI' ! 1 ≡ node λ _ → leaf
-- test-tI'2 = refl
-- test-tI'3 : tI' ! 3 ≡ node λ _ → node λ _ → node λ _ → leaf
-- test-tI'3 = refl
-- test-tI'4 : tI' ! 5 ≡ node λ _ → node λ _ → node λ _ → node λ _ → node λ _ → leaf
-- test-tI'4 = refl
