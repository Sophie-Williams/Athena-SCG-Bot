#!/usr/bin/env python

import warnings
warnings.simplefilter("ignore", DeprecationWarning)

from distutils.core import setup, Extension
from Pyrex.Distutils import build_ext
import os

os.environ['CFLAGS'] = '-std=c99'

setup(
    name='Athena',
    version='0.0.0',
    description='Bot Robot Bot',
    author='William Nowak, Alex Lee',
    author_email='wan@ccs.neu.edu, lee@ccs.neu.edu',
    url='',
    ext_package='relation',
    ext_modules=[Extension('relation', ['src/relation/relation.pyx'])],
    cmdclass={'build_ext': build_ext},
    package_dir={'athena': 'src', 'relation': 'src/relation'},
    packages=['athena', 'relation'],
    requires=['Pyrex (>0.9.8)', 'pyparsing', 'gflags', 'web.py'],
    scripts=['scripts/athena.py'],
)
