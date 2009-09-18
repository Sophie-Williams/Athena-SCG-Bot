
RANK_MIN = 1
RANK_MAX = 5


class Relation(object):

  def __init__(relation_number, rank):
    if rank < RANK_MIN or rank > RANK_MAX:
      raise Exception('')
    self.number = number
    self.rank = rank
    self.mask = self.get_mask(rank)

  @staticmethod
  def get_mask(rank):
    """
    >>> Relation.get_mask(0)
    1
    >>> Relation.get_mask(1)
    3
    >>> Relation.get_mask(2)
    15
    >>> Relation.get_mask(3)
    255
    >>> Relation.get_mask(4)
    65535
    >>> Relation.get_mask(5)
    4294967295L
    """
    return (0x1, 0x3, 0xF, 0xFF, 0xFFFF, 0xFFFFFFFF)[rank]

  def get_magic_number(self, var_p, value):
    """Returns a magic number associated with a certain truth table
    column and value. The magic number associated with column number 0
    of the truth table and value 0 is basically a sequence of alternating
    0 and 1 bits, packed together in one integer. The magic number
    associated with column number 0 of the truth table and value 1 is a
    sequence of alternating 1 and 0 bits, packed together in one integer.
    In general: (getMagicNumber(r,n,0) == ~getMagicNumber(r,n,1))
    For column 1: magic numbers are fromed from sequences of two 0's followed by two 1's
    For column 2: magic numbers are formed from sequences of four 0's followed by four 1's
    For column 3: magic numbers are formed from sequeneces of eight 0's followed by eight 1's
    For column 4:magic numbers are formed from sequeneces of sixteen 0's followed by sixteen 1's
    There is no other possible columns as long as we are using 32 bit integers
    @param rank The rank of the relation, used to determine the truth table height and to check
    the position argument
    @param variablePosition the position of the desired magic number
    @param value the value associated with the desired magic number
    @return
    @throws IllegalArgumentException
    """
    # check{var_p, value}

    # Truth Table order
    # each entry in this array represents a packed truth table column
    magic_numbers = (
      0x55555555, 0x33333333, 0x0F0F0F0F, 0x00FF00FF, 0x0000FFFF,
    )

    # cut the truth table column
    masked_col = magic_numbers[var_p] & self.mask

    if value == 1:
      return mask ^ masked_col
    return masked_col
