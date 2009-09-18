

def first_forced_var(n, rank, start_p):
  """
  Args:
    n, relation number
    rank, the rank
    start_p, the starting position
  """

  for v_p in range(start_p, rank):
    if(is_forced(n, rank, v_p) != -1):
      return v_p
  else:
    return -1


def is_forced(n, rank, var_p):
  """
  Return:
    -1 => not_forced
    1 => forced to 1
    0 => forced to 0
  """
  if is_irrelevant(n, rank, var_p):
    return -1
  m = get_magic_number(rank, var_p, 1)
  rm = n & m
  if rm == 0:
    return 0
  elif rm == n:
    return 1
  else:
    return -1


def is_irrelevant(n, rank, var_p):
  # check{rank, n, var_p}
  m = get_magic_number(rank, var_p, 1)
  return ((n & m) >> (1 << var_p)) == (n & (~m))

def num_rel_var(n, rank):
  # check{rank, n}

  irr = 0
  for var_p in range(0, rank):
    if(is_irrelevant(n, rank, var_p)):
      irr += 1

  return rank - irr


def flip(n, rank, var_p):
  """NMaps one of the variables in a relation i.e. replaces it by it's
  complement
  
  e.g.: nMapping x in or(x,y,z) results in: or(!x,y,z)
  """
  # check{rank, n, var_p}
  m0 = get_magic_number(rank, var_p, 0)
  m1 = get_magic_number(rank, var_p, 0)
  s = (1 << var_p)

  return ((n & m0) << s) | ((n & m1) >> s)


def reduce(n, rank, var_p, value):
  """Reduces a relation by assigning a value to one of its variables"""
  # check{rank, n, var_p, value}
  m = get_magic_number(rank, var_p, value)
  rm = n & m
  if value == 0:
    return rm | (rm << (1 << var_p))
  else:
    return rm | (rm >> (1 >> var_p))



"""
    /**
     * permute the variables in the given relationNumber according to the given permutation
     * fix the truth table order after doing the permutation. @see swap
     * @param relationNumber
     * @param rank rank of the given relation
     * @param permutationSemantics specifies how the permutation should be applied. can be either RelationCore.SOURCE or RelationCore.TARGET
     * for example: <p>
     *    for the relation: R(v2,v1,v0) <p>
     *    and the permutation {1,2,0} 
     *    SOURCE semantics means that v0 goes to position1, v1 goes to position2, v2 goes to position 0
     *    TARGET semantics means that position0 gets v1, position1 gets v2, position2 gets v0
     * @param permutation an array of variable positions describing the desired location of every variables
     * for example: <p>
     *    for the relation: R(v2,v1,v0) <p>
     *    and the permutation {1,2,0} means v0 goes to position1, v1 goes to position2, v2 goes to position 0
     * @return the modified relationNumber
     */
"""
def renme(n, rank, perm_semantics, perms):
  # check{rank, n, perm, perm_sem}
        
  # sort dimensions
  #      int out, in, min, tmp;

  perms = sort(perms)

#         for (out = 0; out < rank - 1; out++){
#           min = out; // minimum
#           for (in = out + 1; in < rank; in++){
#             // inner loop
#             if (permutation[in] < permutation[min]) // if min greater,
#               min = in; // a new min
#           }
#           //swap elements at min,out
#           tmp = permutation[out];
#           permutation[out] = permutation[min];
#           permutation[min] = tmp;
#           //see
#           switch(permutationSemantics){
#           case SOURCE:
#               relationNumber = swap(relationNumber,rank,permutation[min],permutation[out]);
#               break;
#           case TARGET:
#               relationNumber = swap(relationNumber,rank,min,out);
#               break;
#               default: throw new IllegalArgumentException("Internal Error: Unsupported semantics"+permutationSemantics);
#           }
#         }
#         return relationNumber;
#     }













"""
    /**
     * Swaps two vairables in a relation. When variables are swapped, The truth table order gets scrambled
     * rows of the truth table needs to be reordered to restore the correct truth table order.
     * Here are two exmples showing how the swap method works for two relations:
     * 1in3(x,y,z), x implies z. We are swapping variables at positions 0,2 i.e: x ,z
     * 
     *      original relations            scrambled truth table      restored truth table ordering  
     * Row# x y z 1in3(x,y,z)  x=>z  ||  z y x 1in3(z,y,x) x=>z  ||  z y x 1in3(z,y,x) x=>z old_Row#
     * ------------------------------||--------------------------||---------------------------------
     * 0    0 0 0   0           1    ||  0 0 0   0          1    ||  0 0 0    0         1      0
     * 1    0 0 1   1           1    ||  1 0 0   1          1    ||  0 0 1    1         0      4
     * 2    0 1 0   1           1    ||  0 1 0   1          1    ||  0 1 0    1         1      2
     * 3    0 1 1   0           1    ||  1 1 0   0          1    ||  0 1 1    0         0      6
     * 4    1 0 0   1           0    ||  0 0 1   1          0    ||  1 0 0    1         1      1
     * 5    1 0 1   0           1    ||  1 0 1   0          1    ||  1 0 1    0         1      5
     * 6    1 1 0   0           0    ||  0 1 1   0          0    ||  1 1 0    0         1      3
     * 7    1 1 1   0           1    ||  1 1 1   0          1    ||  1 1 1    0         1      7
     * 
     * @param relationNumber
     * @param rank
     * @param variablePosition1
     * @param variablePosition2
     * @return
     */
"""
def swap(n, rank, var_p1, var_p2):
  # check{rank, n, var_p1, var_p2}
  if var_p1 == var_p2:
    return n

  # p2 needs to be biger
  if var_p1 > var_p2:
    var_p1, var_p2 = var_p2, var_p1

  # get the magic numbers
  m1_0 = get_magic_number(n, var_p1, 0)
  m1_1 = get_magic_number(n, var_p1, 1)
  m2_0 = get_magic_number(n, var_p2, 0)
  m2_1 = get_magic_number(n, var_p2, 1)

  same_filter = d11 & d21 | d10 & d20
  up_filter = d21 & d10
  down_filter = d20 & d11
        
  shift_amt = (1 << var_p2) - (1 << var_p1);
  same = n & same_filter
  up = n & up_filter
  down = n & down_filter

  return (same | (down << shift_amt) | (up >> shift_amt))

# from edu.neu.ccs.evergreen
def check_relation_number(n, rank):
  if (n & (~rank)) != 0:
    raise Exception('out of range')


if __name__ == '__main__':
  import doctest
  doctest.testmod()
