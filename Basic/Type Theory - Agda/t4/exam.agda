-- BEGIN FIX
open import Agda.Primitive

open import Agda.Builtin.Nat
  renaming (Nat to ℕ)
  public
open import Agda.Primitive
Type = Set
open import Agda.Builtin.Equality
  public
open import Agda.Builtin.Bool
  public
open import Agda.Builtin.Sigma
  public

infixr 4 _,_
infixr 2 _×_
infixr 1 _⊎_
infix 0 _↔_

if_then_else_ : ∀{i}{A : Set i}(t : Bool)(u v : A) → A
if true  then u else v = u
if false then u else v = v

-- Product types
record _×_ {i}{j}(A : Set i)(B : Set j) : Set (i ⊔ j) where
  constructor _,_
  field
    fst : A
    snd : B
open _×_ public

-- Sum types
data _⊎_ {i}{j}(A : Set i)(B : Set j) : Set (i ⊔ j) where
  inl : A → A ⊎ B
  inr : B → A ⊎ B

case : ∀ {i j k}{A : Set i}{B : Set j}{C : Set k}
         (t : A ⊎ B)(u : A → C)(v : B → C) → C
case (inl t) u v = u t
case (inr t) u v = v t

-- Empty type
data ⊥ : Set where

exfalso : ∀{i}{A : Set i} → ⊥ → A
exfalso ()

-- Unit type
record ⊤ : Set where
  constructor tt
open ⊤ public

_↔_ : ∀{i j} → Set i → Set j → Set (i ⊔ j)
A ↔ B = (A → B) × (B → A)

¬_ : ∀{i} → Set i → Set i
¬ A = A → ⊥

sym : ∀{i}{A : Set i}{x y : A} → x ≡ y → y ≡ x
sym refl = refl

trans : ∀{i}{A : Set i}{x y z : A} → x ≡ y → y ≡ z → x ≡ z
trans refl refl = refl

cong : ∀{i j}{A : Set i}{B : Set j}(f : A → B){x y : A} → x ≡ y → f x ≡ f y
cong f refl = refl

transp : ∀{i j}{A : Set i}(P : A → Set j){x y : A} → x ≡ y → P x → P y
transp P refl p = p

ass+ : (a b c : ℕ) → (a + b) + c ≡ a + (b + c)
ass+ zero    b c = refl
ass+ (suc a) b c = cong suc (ass+ a b c)

idr+ : (a : ℕ) → a + 0 ≡ a
idr+ zero    = refl
idr+ (suc a) = cong suc (idr+ a)

+suc : (a b : ℕ) → suc a + b ≡ a + suc b
+suc zero    b = refl
+suc (suc a) b = cong suc (+suc a b)

comm+ : (a b : ℕ) → a + b ≡ b + a
comm+ zero b = sym (idr+ b)
comm+ (suc a) b = trans (cong suc (comm+ a b)) (+suc b a)

_≤_ : ℕ → ℕ → Set
x ≤ y = Σ ℕ λ m → m + x ≡ y

pred : ℕ → ℕ
pred zero = zero
pred (suc n) = n
-- END FIX

-- BEGIN FIX
-- n1-et és n2-t úgy kell megadni, hogy n1 ℕ suc (λ _ → zero) ≠ n2 ℕ suc (λ _ → zero)
n1 n2 : (A : Set) → (A → A) → (⊤ → A) → A
-- END FIX
n1 A x y = y tt
n2 A x y = x (y tt)
-- BEGIN FIX
test-n1-n2 : ¬ (n1 ℕ suc (λ _ → zero) ≡ n2 ℕ suc (λ _ → zero))
test-n1-n2 ()
-- END FIX

-- BEGIN FIX
iso'' : (X Y Z : Set) → (X → (Z × Y)) ↔ ((X → Y ⊎ ⊥) × (X ⊎ ⊥ → Z))
-- END FIX
fst (fst (iso'' X Y Z) x) y = inl ((x y) .snd)
snd (fst (iso'' X Y Z) x) (inl y) = (x y) .fst
fst (snd (iso'' X Y Z) (a , b) y) = b (inl y)
snd (snd (iso'' X Y Z) (a , b) y) = case (a y) (λ c → c) λ()

-- BEGIN FIX
fullCurry : (A : Set)(B : A → Set)(C : (x : A) → B x → Set) → ((x : A)(y : B x) → C x y) ↔ ((w : Σ A B) → C (fst w) (snd w))
-- END FIX
fst (fullCurry A B C) x (a , b) = x a b
snd (fullCurry A B C) x y z = x (y , z)

-- BEGIN FIX
dnp : (A : Set) → ¬ ¬ (¬ ¬ A → A)
-- END FIX
dnp A x = {!x λ a → a λ b → b!}

-- BEGIN FIX
tr' : (a b : ℕ) → ((P : ℕ → Set) → P a → P b) → a + b ≡ a + a
-- END FIX
tr' zero b x = {!!}
tr' (suc a) b x = {!!}

-- BEGIN FIX
eq1 : (x y : ℕ) → y + (x + y) ≡ (y + 0) + (y + x)
-- END FIX
eq1 x y = trans (sym (ass+ y x y))
                (trans (sym (idr+ (y + x + y)))
                (trans (ass+ (y + x) y 0)
                (trans (comm+ (y + x) (y + 0)) refl)))

-- BEGIN FIX
plusEq : (x y : ℕ) → x + y ≡ x → y ≡ 0
-- END FIX
plusEq x y z = {!leaveX z!}
             where
             leaveX : (x y : ℕ) → x + y ≡ x → y ≡ 0
             leaveX x y z = let x = 0 in {!z!}

-- BEGIN FIX
lemma6 : ¬ ((n : ℕ) → ¬ (3 ≡ n) → n ≡ 2)
-- END FIX
lemma6 x = {!sol (x 1)!}
                    where
                    sol : ¬ (1 ≡ 2)
                    sol ()


-- BEGIN FIX
ffg : ¬ ((R : ℕ × ℕ → Set) → (Σ (ℕ × ℕ) R) → Σ ℕ (λ x → R (x , x)))
-- END FIX
ffg x = {!!}

-- BEGIN FIX
_>500 : ℕ → Bool
-- END FIX
x >500 = if x > 500 then true else false
                where
                _>_ : ℕ → ℕ → Bool
                _>_ zero zero = false
                _>_ n zero = true
                _>_ zero m = false
                _>_ (suc n) (suc m) = _>_ n m
-- BEGIN FIX
test->500-1 : 3 >500 ≡ false
test->500-1 = refl
test->500-2 : 500 >500 ≡ false
test->500-2 = refl
test->500-3 : 501 >500 ≡ true
test->500-3 = refl
test->500-4 : 6000 >500 ≡ true
test->500-4 = refl
-- END FIX

