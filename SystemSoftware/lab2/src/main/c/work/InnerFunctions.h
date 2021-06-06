#pragma once
#include "NTFS.h"

int copyByInode(NTFSInfoStruct* ntfs, ntfs_inode* inode, char* outputPath);