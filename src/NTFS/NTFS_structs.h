#ifndef __NTFS_STRUCTS__
#define __NTFS_STRUCTS__

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
 * Partiotion boot sector:
 *    3B    8B      25B
 * |-----+--------+-----
 * | JMP | OEM ID | BPB
 * |-----+--------+------
 * 
*/

typedef struct __attribute__((packed)) pbp {
    u16 bytes_per_sector;   // bytes per sector
    u8 sectors_per_cluster; //sectors per_cluster
    u16 reserver_sectors;   //Always ZERO
    u8 zeroes0[3];          //Must be zero(all)
    u16 zero0;              //Must be zero
    u8 media_descriptor;    // f8 - hard disk, f0 - high-density 3.5 inch floppy-disk, not used
    u16 zero1;              //Must be zero
    u16 not_used0;          //Not used
    u16 not_used1;          //Not used
    u32 not_used2;          //Not used
    u32 zero2;              //Must be zero
}BIOS_parameter_block;

typedef struct __attribute__((packed)) ebpb {
    u32 not_used0;                      //Not used (default 80 00 80 00) or something similar
    u64 total_sectors;                  //Total amount of sectors on the hard disk
    u64 logical_cluster_number;         //Identifies the location of the $MFT by using it's logical cluster number
    u64 logical_cluster_number_mirror;  // Identifies the loation of the $MFTMirror
    //NOTE: important thing \/ - https://docs.microsoft.com/en-us/previous-versions/windows/it-pro/windows-server-2003/cc781134(v=ws.10)
    u8 clusters_per_mft_record;         //The size of each record. ^ - look here. It's also important thing
    u8 not_used1[3];                    //Not used
    u8 clusters_per_index_buffer;       //The size of each index buffer for space for dirs
    u8 not_used2[3];                    //Not used
    u64 serial_number;                  //Volume serial number
    u32 not_used3;                      //Not used

}Extended_BPB;

//Partition Boot Sector
typedef struct __attribute__((packed)) PBSector {
    u8 jump[3];                 /* jump to boot code*/
    le64 oem_id;                /*Magic "NTFS    " (yeah, 4 spaces)*/
    BIOS_parameter_block bpb;   /*See BIOS_parameter block*/
    Extended_BPB ebpb;          /*See Extended_BPB*/

    u8 bootstrap_code[426];     /*Useless, just bootstrap code*/
    le16 end_of_sector_marker;
}PBSector;

typedef struct __attribute__((__packed__)) ntfs_mft_record{
    uint32_t magic;
    uint16_t usa_ofs;
    uint16_t usa_count;
    uint64_t lsn;
    uint16_t seq_no;
    uint16_t link_count;
    uint16_t attrs_offset;
    uint16_t flags;     /* MFT record flags , 0x01 - in use, 0x02 - directory*/
    //TODO: change it all to normal, not copied from linux sources    
}ntfs_mft_record;


#endif