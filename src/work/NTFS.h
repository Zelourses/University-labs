#ifndef __NTFS_STRUCTS__
#define __NTFS_STRUCTS__

#include <stddef.h>
#include "types.h"

/*
 * NTFS has structure like that:
 *      1           2           3                           4
 * |------------+-----------+---------------------------+---------------|
 * | Partition  |   MFT -   |                           |               |
 * |   boot     |   Master  |                           |   MFT copy    |
 * |  sector    |    File   |   System files.....       |               |
 * |            |   Table   |                           |               |
 * |------------+-----------+---------------------------+---------------|
 * First things first, we realize 1
 */


/*
 * Partition boot sector:
 *    3B    8B      25B         48B         426B        == 512B(with end sector marker which size is 2B)
 * |-----+--------+-----+---------------+-----------|
 * | JMP | OEM ID | BPB | extended BIOSParameterBlock  | boot code |
 * |-----+--------+-----+---------------+-----------|
 * 
*/
typedef struct __attribute__((packed)) {
    u16 bytes_per_sector;   // bytes per sector
    u8 sectors_per_cluster; //sectors per_cluster
    u16 reserved_sectors;   //Always ZERO
    u8 zeroes0[3];          //Must be zero(all)
    u16 zero0;              //Must be zero
    u8 media_descriptor;    // f8 - hard disk, f0 - high-density 3.5 inch floppy-disk, not used
    u16 zero1;              //Must be zero
    u16 not_used0;          //Not used
    u16 not_used1;          //Not used
    u32 not_used2;          //Not used
    u32 zero2;              //Must be zero
}BIOSParameterBlock;

typedef struct __attribute__((packed)){
    u32 not_used0;                      //Not used (default 80 00 80 00) or something similar
    u64 total_sectors;                  //Total amount of sectors on the hard disk
    u64 logical_cluster_number;         //Identifies the location of the $MFT by using it's logical cluster number
    u64 logical_cluster_number_mirror;  // Identifies the loation of the $MFTMirror
    //NOTE: important thing \/ - https://docs.microsoft.com/en-us/previous-versions/windows/it-pro/windows-server-2003/cc781134(v=ws.10)
    s8 clusters_per_mft_record;         //The size of each record. ^ - look here. It's also important thing
    u8 not_used1[3];                    //Not used
    u8 clusters_per_index_buffer;       //The size of each index buffer for space for dirs
    u8 not_used2[3];                    //Not used
    u64 serial_number;                  //Volume serial number
    u32 not_used3;                      //Not used

}extendedBPB;

//Partition Boot Sector
typedef struct __attribute__((packed)) {
    u8 jump[3];                 /* jump to boot code*/
    le64 oem_id;                /*Magic "NTFS    " (yeah, 4 spaces)*/
    BIOSParameterBlock bpb;   /*See BIOS_parameter block*/
    extendedBPB ebpb;          /*See extendedBPB*/
    u8 bootstrap_code[426];     /*Useless, just bootstrap code*/
    le16 end_of_sector_marker;
}PBSector;


typedef struct __attribute__((packed)) n_i{
    u32 mft_number;
    char* filename;
    u16 type;
    struct n_i* parent;
    struct n_i* next;
}ntfs_inode;


// The $MFT metadata file types,  from https://github.com/joyent/syslinux/blob/master/core/fs/ntfs/ntfs.h
typedef enum {
    FILE_MFT            = 0,
    FILE_MFTMirr        = 1,
    FILE_LogFile        = 2,
    FILE_Volume         = 3,
    FILE_AttrDef        = 4,
    FILE_root           = 5,
    FILE_Bitmap         = 6,
    FILE_Boot           = 7,
    FILE_BadClus        = 8,
    FILE_Secure         = 9,
    FILE_UpCase         = 10,
    FILE_Extend         = 11,
    FILE_reserved12     = 12,
    FILE_reserved13     = 13,
    FILE_reserved14     = 14,
    FILE_reserved15     = 15,
    FILE_reserved16     = 16,
}ntfs_system_file;

//Flags, from https://github.com/joyent/syslinux/blob/master/core/fs/ntfs/ntfs.h
enum {
    MFT_RECORD_IN_USE       = 0x0001,
    MFT_RECORD_IS_DIRECTORY = 0x0002,
} __attribute__((__packed__));

typedef struct __attribute__((packed)) {
    u32 magic;
    u16 US_offset;
    u16 US_words_size;
    u64 logfile_seq_number;
    u16 seq_number;
    u16 hard_links_count;
    u16 first_attrib_offset;
    u16 flags;
    u32 size_in_bytes;
    u32 size_allocated;
    u64 base_reference;
    u16 next_attrib_id;
    u16 align_to_4_bytes;   // in XP
    u32 mft_record_number;
}mft_record;


//Struct with all necessary information that we need to know about NTFS partition
//NOTE: Remember to change work.c:stuffNTFSInfoStruct() every time to make this struct always valid!
typedef struct __attribute__((packed)){
    u16 bytes_per_sector;
    s32 sector_shift;
    u64 mft_record_size;
    u32 cluster_byte_shift;
    u32 cluster_size;
    u32 block_shift;
    u32 block_size;
    u64 mft_logical_cluster_number;
    ntfs_inode* current_inode;
    ntfs_inode* root_inode;
    int fd;
}NTFSInfoStruct;



/*
 *      Section with useful structs for work with files.
*/



#define NTFS_MAX_FILENAME_LENGTH 255 //Hmm
#define NTFS_MFT_REF_MASK 0x0000ffffffffffff

typedef enum {
    /* Found in $MFT/$DATA */
    NTFS_MAGIC_FILE     = 0x454C4946,   /* MFT entry */
    NTFS_MAGIC_INDX     = 0x58444E49,   /* Index buffer */
    NTFS_MAGIC_HOLE     = 0x454C4F48,

    /* Found in $LogFile/$DATA */
    NTFS_MAGIC_RSTR     = 0x52545352,
    NTFS_MAGIC_RCRD     = 0x44524352,
    /* Found in $LogFile/$DATA (May be found in $MFT/$DATA, also ?) */
    NTFS_MAGIC_CHKDSK   = 0x444B4843,
    /* Found in all ntfs record containing records. */
    NTFS_MAGIC_BAAD     = 0x44414142,
    NTFS_MAGIC_EMPTY    = 0xFFFFFFFF,   /* Record is empty */
}MFT_RECORD_TYPES;

enum {
    INDEX_ENTRY_NODE            = 1,
    INDEX_ENTRY_END             = 2,
    INDEX_ENTRY_STRANGE         = 0,
    /* force enum bit width to 16-bit */
    INDEX_ENTRY_SPACE_FILTER    = 0xFFFF,
} __attribute__((__packed__));


/*
 * All NTFS attribute names
 * ATTR - attribute
 * from https://github.com/joyent/syslinux/blob/master/core/fs/ntfs/ntfs.h
*/
typedef enum {
    NTFS_ATTR_UNUSED                      = 0x00,
    NTFS_ATTR_STANDARD_INFORMATION        = 0x10,
    NTFS_ATTR_ATTR_LIST                   = 0x20,
    NTFS_ATTR_FILENAME                    = 0x30,
    NTFS_ATTR_OBJ_ID                      = 0x40,
    NTFS_ATTR_SECURITY_DESCP              = 0x50,
    NTFS_ATTR_VOL_NAME                    = 0x60,
    NTFS_ATTR_VOL_INFO                    = 0x70,
    NTFS_ATTR_DATA                        = 0x80,
    NTFS_ATTR_INDEX_ROOT                  = 0x90,
    NTFS_ATTR_INDEX_ALLOCATION            = 0xA0,
    NTFS_ATTR_BITMAP                      = 0xB0,
    NTFS_ATTR_REPARSE_POINT               = 0xC0,
    NTFS_ATTR_EA_INFO                     = 0xD0,
    NTFS_ATTR_EA                          = 0xE0,
    NTFS_ATTR_PROPERTY_SET                = 0xF0,
    NTFS_ATTR_LOGGED_UTIL_STREAM          = 0x100,
    NTFS_ATTR_FIRST_USER_DEFINED_ATTR     = 0x1000,
    NTFS_ATTR_END                         = 0xFFFFFFFF,
}NTFS_ATTR_TYPES;

/*
 * ntfs_attribute_record - Attribute record header, important,
 * because used almost in everywhere in this system.
*/
typedef struct __attribute__((packed)){
    u32 attr_type;
    u32 length;
    u8 non_resident; /*value = 0 if resident, otherwise value = 1 */
    u8 name_length; /*0x00 if doesn't have name*/
    u16 offset_to_name; /*0x00 if has no name*/
    u16 flags;
    u16 attribute_id;
    union {
        struct {
            u32 length_of_attribute;
            u16 offset_to_attribute;
            u8 indexed_flag; /* Flags of resident attributes */
            u8 padding;
        }__attribute__((__packed__)) resident;
        struct {
            u64 starting_vcn; /*VCN - virtual cluster number*/
            u64 last_vcn;
            u16 offset_to_data_runs;
            u16 compression_unit_size;
            u32 padding;    /*
                             * Funny thing, it's actually align to 8 byte-boundary and
                             * also in sources it's array of 5 bytes
                            */
            u64 allocated_size;
            u64 real_size;
            u64 initialized_size;
            u64 compressed_size;    /*
                                     * FIXME: I am not sure about this field, because it's represent only in compressed
                                     *  files, and there could be situation where I would delete it.
                                    */
        }__attribute__((__packed__)) non_resident;

    }__attribute__((__packed__)) data;
}ntfs_attribute_record;

typedef struct __attribute__((__packed__)) {
    u32 offset_fo_first_index_entry;
    u32 index_entries_length;
    u32 allocated_size;
    u8 non_leaf;
    u8 flags;

    u8 reserved[3];                     /* only for aligning */
}ntfs_index_node_header;

/*
 * ntfs_index_root - Attribute $INDEX_ROOT (0x90)
 *  NOTE: always resident, heh
 *
 *
 *  NOTE: root folder contains entry for itself
 */
typedef struct __attribute__((__packed__)){
    u32 attr_type;
    u32 collation_rule;             /* used for sorting index entries */
    u32 bytes_per_index_record;     /* size of index record in bytes */
    u8 clusters_per_index_record;   /* size of index record in clusters, when record >= than size of cluster */
    u8 reserved[3];                 /* only of aligning */

    ntfs_index_node_header index;
}ntfs_index_root;


/* Attribute: Filename (0x30)
 * Note: always resident
 * from https://github.com/joyent/syslinux/blob/master/core/fs/ntfs/ntfs.h
 */
typedef struct __attribute__((__packed__)) {
    u64 parent_directory;
    s64 ctime;
    s64 atime;
    s64 mtime;
    s64 rtime;
    u64 allocated_size;
    u64 data_size;
    u32 file_attrs;
    union {
        struct {
            u16 packed_ea_size;
            u16 reserved;      /* reserved for alignment */
        } __attribute__((__packed__)) ea;
        struct {
            u32 reparse_point_tag;
        } __attribute__((__packed__)) rp;
    } __attribute__((__packed__)) type;
    u8 file_name_len;
    u8 file_name_type;
    u16 file_name[0];          /* File name in Unicode */
} ntfs_filename_attr;


// index entry, from https://github.com/joyent/syslinux/blob/master/core/fs/ntfs/ntfs.h
typedef struct __attribute__((__packed__)) {
    union {
        struct { /* Only valid when INDEX_ENTRY_END is not set */
            u64 indexed_file;
        } __attribute__((__packed__)) dir;
        struct { /* Used for views/indexes to find the entry's data */
            u16 data_offset;
            u16 data_len;
            u32 reservedV;
        } __attribute__((__packed__)) vi;
    } __attribute__((__packed__)) data;
    u16 len;
    u16 key_len;
    u16 flags;     /* Index entry flags */
    u16 reserved;  /* Align to 8-byte boundary */
    union {
        ntfs_filename_attr file_name;
        //SII_INDEX_KEY sii;
        //SDH_INDEX_KEY sdh;
        //GUID object_id;
        //REPARSE_INDEX_KEY reparse;
        //SID sid;
        u32 owner_id;
    } __attribute__((__packed__)) key;
}ntfs_idx_entry;

/* Attribute: Index allocation (0xA0)
 * Note: always non-resident
 * from https://github.com/joyent/syslinux/blob/master/core/fs/ntfs/ntfs.h
 */
typedef struct __attribute__((__packed__)) {
    uint32_t magic;
    uint16_t usa_ofs;           /* Update Sequence Array offsets */
    uint16_t usa_count;         /* Update Sequence Array number in bytes */
    int64_t lsn;
    int64_t index_block_vcn;    /* Virtual cluster number of the index block */
    ntfs_index_node_header index;
} ntfs_idx_allocation;

typedef struct {
    uint64_t length;
    uint8_t *buf;
    uint8_t current_block;
}mapping_chunk;

typedef struct {
    uint8_t resident;
    uint64_t length;
    uint64_t blocks_count;
    uint8_t *buf;
    int64_t *lcns;
    uint64_t *lengths;
    int cur_lcn;
    int lcn_count;
    unsigned long cur_block;
    int signal;
} mappingChunkData;

typedef struct {
    ntfs_inode* start;
    ntfs_inode* result;
}NTFSFindInfo;

int ntfsFindAttributes(NTFSInfoStruct* ntfs,
                       u32 attribute_type, /* Type of attribute, see: NTFS_ATTR_TYPES */
                         mft_record* record, /* Record from which we trying to find attributes*/
                         ntfs_attribute_record** attribute_record /* Where to place it*/
);

int findNodeByName(NTFSInfoStruct* ntfs,
                   char* path, ntfs_inode** startingInode, NTFSFindInfo** result);

u64 mftFindRecord(NTFSInfoStruct* ntfs, u32 file, mft_record** record);

int ntfsReadDir(NTFSInfoStruct* ntfs, ntfs_inode** inode);
void freeInode(ntfs_inode* inode);

int ntfsReadFileData(NTFSInfoStruct* ntfs, mappingChunkData** chunk, ntfs_inode* inode);

void ntfsFreeDataChunk(mappingChunkData* chunkData);

int readBlockFile(NTFSInfoStruct* ntfs, mappingChunkData** chunk);

#endif
