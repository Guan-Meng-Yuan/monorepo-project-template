#!/usr/bin/env node

import inquirer from 'inquirer';
import { spawn } from 'child_process';

async function selectBuildMode() {
  const questions = [
    {
      type: 'list',
      name: 'buildMode',
      message: '请选择构建模式:',
      choices: [
        { name: '微信小程序', value: 'weixin' },
        { name: '支付宝小程序', value: 'alipay' },
        { name: '微信 + 支付宝小程序 (同时构建)', value: 'both' },
        { name: '仅后端', value: 'backend' },
        { name: '全栈 (后端 + 微信 + 支付宝)', value: 'full' },
      ],
    },
  ];

  try {
    const answers = await inquirer.prompt(questions);
    const { buildMode } = answers;

    console.log(`\n🏗️  开始构建: ${buildMode} ...\n`);

    switch (buildMode) {
      case 'weixin':
        runCommand('pnpm', ['build:frontend:weixin']);
        break;
      case 'alipay':
        runCommand('pnpm', ['build:frontend:alipay']);
        break;
      case 'both':
        runCommand('pnpm', ['build:frontend:both']);
        break;
      case 'backend':
        runCommand('pnpm', ['build:backend']);
        break;
      case 'full':
        runCommand('pnpm', ['build:full']);
        break;
      default:
        console.log('❌ 无效的选择');
        process.exit(1);
    }
  } catch (error) {
    console.error('❌ 选择过程中出现错误:', error.message);
    process.exit(1);
  }
}

function runCommand(command, args, env = {}) {
  const child = spawn(command, args, {
    stdio: 'inherit',
    shell: true,
    env: { ...process.env, ...env },
  });

  child.on('error', (error) => {
    console.error(`❌ 执行命令时出错: ${error.message}`);
    process.exit(1);
  });

  child.on('exit', (code) => {
    if (code !== 0) {
      console.error(`❌ 命令执行失败，退出码: ${code}`);
      process.exit(code);
    }
  });
}

// 运行选择器
selectBuildMode();


