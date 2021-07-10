/*
 Navicat Premium Data Transfer

 Source Server         : db_local
 Source Server Type    : MySQL
 Source Server Version : 100411
 Source Host           : localhost:3306
 Source Schema         : ecommerce

 Target Server Type    : MySQL
 Target Server Version : 100411
 File Encoding         : 65001

 Date: 11/07/2021 00:50:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chart
-- ----------------------------
DROP TABLE IF EXISTS `chart`;
CREATE TABLE `chart`  (
  `items_iditems` int(11) NOT NULL,
  `pembeli_idpembeli` int(11) NOT NULL,
  `jumlah` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`items_iditems`, `pembeli_idpembeli`) USING BTREE,
  INDEX `fk_items_has_pembeli_pembeli1_idx`(`pembeli_idpembeli`) USING BTREE,
  INDEX `fk_items_has_pembeli_items_idx`(`items_iditems`) USING BTREE,
  CONSTRAINT `fk_items_has_pembeli_items` FOREIGN KEY (`items_iditems`) REFERENCES `items` (`iditems`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_items_has_pembeli_pembeli1` FOREIGN KEY (`pembeli_idpembeli`) REFERENCES `pembeli` (`idpembeli`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for items
-- ----------------------------
DROP TABLE IF EXISTS `items`;
CREATE TABLE `items`  (
  `iditems` int(11) NOT NULL,
  `nama` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `harga` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`iditems`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of items
-- ----------------------------
INSERT INTO `items` VALUES (1, 'panadol', '8000');
INSERT INTO `items` VALUES (2, 'paramek', '7500');
INSERT INTO `items` VALUES (3, 'mixagrip', '6000');

-- ----------------------------
-- Table structure for pembeli
-- ----------------------------
DROP TABLE IF EXISTS `pembeli`;
CREATE TABLE `pembeli`  (
  `idpembeli` int(11) NOT NULL,
  `nama` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`idpembeli`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pembeli
-- ----------------------------
INSERT INTO `pembeli` VALUES (1, 'Teguh', '123');
INSERT INTO `pembeli` VALUES (2, 'Udin', '123');
INSERT INTO `pembeli` VALUES (3, 'Budi', '456');

SET FOREIGN_KEY_CHECKS = 1;
