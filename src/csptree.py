import rbtree

vars1 = ['v1', 'v2', 'v3', 'v4']
data1 = [1,0,1,1]

class cspnode(rbtree.rbnode):
  def __init__(self, key, value):
    rbtree.rbnode.__init__(self, key)
    self._value = value

  value = property(fget=lambda self: self._value, doc="value data")

  def __str__(self):
    clrmap = {0:'black', 1:'red'}
    if str(self._right) == 'None':
      r = ''
    else:
      r = str(self._right)
    if str(self._left) == 'None':
      l = ''
    else:
      l = str(self._left)
    return ('(node %s (%s -> %s) %s %s)'
            % (clrmap[self._red], self._key, self._value, r, l))

class csptree(rbtree.rbtree):

  def insert_keyvalue(self, key, value):
    self.insert_node(cspnode(key, value))

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

s = csptree.CreateSolution(vars1, data1)
