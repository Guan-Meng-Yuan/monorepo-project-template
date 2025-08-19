#!/usr/bin/env node

import inquirer from 'inquirer';
import { spawn } from 'child_process';

async function selectDevMode() {
  const questions = [
    {
      type: 'list',
      name: 'devMode',
      message: '请选择开发模式:',
      choices: [
        {
          name: '微信小程序',
          value: 'weixin'
        },
        {
          name: '支付宝小程序',
          value: 'alipay'
        },
        {
          name: '微信 + 支付宝小程序 (同时运行)',
          value: 'both'
        },
        {
          name: '仅后端',
          value: 'backend'
        },
        {
          name: '全栈 (后端 + 微信 + 支付宝)',
          value: 'full'
        }
      ]
    }
  ];

  try {
    const answers = await inquirer.prompt(questions);
    const { devMode } = answers;

    // 如果选择了包含后端的选项，询问环境
    if (devMode === 'backend' || devMode === 'full') {
      const envQuestions = [
        {
          type: 'list',
          name: 'environment',
          message: '请选择运行环境:',
          choices: [
            {
              name: '开发环境 (dev)',
              value: 'dev'
            },
            {
              name: '测试环境 (test)',
              value: 'test'
            },
            {
              name: '生产环境 (prod)',
              value: 'prod'
            }
          ]
        }
      ];

      const envAnswers = await inquirer.prompt(envQuestions);
      const { environment } = envAnswers;

      console.log(`\n🚀 启动 ${devMode} 开发模式，环境: ${environment}...\n`);

      switch (devMode) {
        case 'backend':
          runCommand('pnpm', ['dev:backend'], { PROFILE: environment });
          break;
        case 'full':
          runCommand('pnpm', ['dev:full'], { PROFILE: environment });
          break;
        default:
          console.log('❌ 无效的选择');
          process.exit(1);
      }
    } else {
      console.log(`\n🚀 启动 ${devMode} 开发模式...\n`);

      switch (devMode) {
        case 'weixin':
          runCommand('pnpm', ['dev:frontend:weixin']);
          break;
        case 'alipay':
          runCommand('pnpm', ['dev:frontend:alipay']);
          break;
        case 'both':
          runCommand('pnpm', ['dev:frontend:both']);
          break;
        default:
          console.log('❌ 无效的选择');
          process.exit(1);
      }
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
    env: { ...process.env, ...env }
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
selectDevMode();
