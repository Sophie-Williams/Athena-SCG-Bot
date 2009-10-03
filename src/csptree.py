import rbtree

COLORMAP = {0: 'black', 1: 'red'}

class CSPNode(rbtree.rbnode):
  def __init__(self, key, value):
    rbtree.rbnode.__init__(self, key)
    self._value = value

  value = property(fget=lambda self: self._value, doc="value data")

  def __str__(self):
    l = r = ''
    if str(self._left) != 'None':
      l = str(self._left)
    if str(self._right) != 'None':
      r = str(self._right)
    return ('(node %s (%s -> %s) %s %s)'
            % (COLORMAP[self._red], self._key, self._value, l, r))

class CSPTree(rbtree.rbtree):

  def insert_keyvalue(self, key, value):
    self.insert_node(CSPNode(key, value))

  @classmethod
  def CreateSolution(cls, vars, data):
    tfmap = {0:'false', 1:'true'}
    assert len(vars) == len(data)
    tree = cls()
    for x in range(len(vars)):
      tree.insert_keyvalue(vars[x], tfmap[data[x]])
    return tree

  def __str__(self):
    return str(self.root)
